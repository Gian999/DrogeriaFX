package pe.edu.upeu.drogeriafx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.drogeriafx.componente.ColumnInfo;
import pe.edu.upeu.drogeriafx.dto.ComboBoxOption;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.servicio.FormatoService;
import pe.edu.upeu.drogeriafx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.drogeriafx.componente.TableViewHelper;
import pe.edu.upeu.drogeriafx.componente.Toast;
import pe.edu.upeu.drogeriafx.servicio.TipoFormatoService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class FormatoController {

    @FXML
    TextField txtNombreFormato, txtDescripcion;
    @FXML
    ComboBox<ComboBoxOption> cbxTipoFormato;
    @FXML
    private TableView<Formato> formatoTable;
    @FXML
    Label lbnMsg;
    @FXML
    private VBox miContenedor;
    Stage stage;

    @Autowired
    FormatoService formatoService;

    private Validator validator;
    ObservableList<Formato> listarFormato;
    Formato formulario;
    Long idFormatoCE = 0L;
    @Autowired
    private TipoFormatoService tipoFormatoService;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        cbxTipoFormato.setTooltip(new Tooltip());
        cbxTipoFormato.getItems().addAll(tipoFormatoService.listarCombobox());
        cbxTipoFormato.setOnAction(event -> {
            ComboBoxOption selectedOption = cbxTipoFormato.getSelectionModel().getSelectedItem();
            if (selectedOption != null) {
                String selectedId = selectedOption.getKey();
                System.out.println("ID del tipo de formato seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxTipoFormato);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Formato> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idFormato", 60.0));
        columns.put("Nombre Formato", new ColumnInfo("nombre", 200.0));
        columns.put("Descripción", new ColumnInfo("descripcion", 300.0));

        Consumer<Formato> updateAction = (Formato formato) -> {
            System.out.println("Actualizar: " + formato);
            editForm(formato);
        };
        Consumer<Formato> deleteAction = (Formato formato) -> {
            formatoService.delete(formato.getIdFormato());
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(formatoTable, columns, updateAction, deleteAction);

        formatoTable.setTableMenuButtonVisible(true);
        listar();
    }

    public void listar() {
        try {
            formatoTable.getItems().clear();
            listarFormato = FXCollections.observableArrayList(formatoService.list());
            formatoTable.getItems().addAll(listarFormato);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpiarError() {
        txtNombreFormato.getStyleClass().remove("text-field-error");
        txtDescripcion.getStyleClass().remove("text-field-error");
        cbxTipoFormato.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtNombreFormato.setText("");
        txtDescripcion.setText("");
        cbxTipoFormato.getSelectionModel().select(null);
        idFormatoCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    @FXML
    public void validarFormulario() {
        formulario = new Formato();
        formulario.setNombre(txtNombreFormato.getText());
        formulario.setDescripcion(txtDescripcion.getText());
        String idxTipoFormato = cbxTipoFormato.getSelectionModel().getSelectedItem() == null ? "0" : cbxTipoFormato.getSelectionModel().getSelectedItem().getKey();
        formulario.setIdFormato(formatoService.searchById(Long.parseLong(idxTipoFormato)));

        Set<ConstraintViolation<Formato>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Formato>> violacionesOrdenadas = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadas.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;

            if (idFormatoCE != 0L && idFormatoCE > 0L) {
                formulario.setIdFormato(idFormatoCE);
                formatoService.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
            } else {
                formatoService.save(formulario);
                Toast.showToast(stage, "Se registró correctamente!!", 2000, with, h);
            }
            clearForm();
            listar();
        } else {
            mostrarErrores(violacionesOrdenadas);
        }
    }

    void mostrarErrores(List<ConstraintViolation<Formato>> violacionesOrdenadas) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Formato> violacion : violacionesOrdenadas) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("nombre")) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNombreFormato.getStyleClass().add("text-field-error");
            } else if (campo.equals("descripcion")) {
                erroresOrdenados.put("descripcion", violacion.getMessage());
                txtDescripcion.getStyleClass().add("text-field-error");
            } else if (campo.equals("tipoFormato")) {
                erroresOrdenados.put("tipoFormato", violacion.getMessage());
                cbxTipoFormato.getStyleClass().add("text-field-error");
            }
        }
        String mensajeError = erroresOrdenados.values().stream().collect(Collectors.joining("\n"));
        lbnMsg.setText(mensajeError);
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }

    public void editForm(Formato formato) {
        txtNombreFormato.setText(formato.getNombre());
        txtDescripcion.setText(formato.getDescripcion());
        cbxTipoFormato.getSelectionModel().select(cbxTipoFormato.getItems().stream()
                .filter(item -> item.getKey().equals(String.valueOf(formato.getTipoFormato().getIdFormato())))
                .findFirst().orElse(null));
        idFormatoCE = formato.getIdFormato();
    }

    @FXML
    public void agregarFormato() {
        String nombre = txtNombreFormato.getText();
        String descripcion = txtDescripcion.getText();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            Formato nuevoFormato = new Formato();
            nuevoFormato.setNombre(nombre);
            nuevoFormato.setDescripcion(descripcion);
            formatoService.save(formulario); // Guarda el nuevo formato
            listar(); // Actualiza la tabla
            clearForm(); // Limpia los campos



            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            if(idFormatoCE!=0L && idFormatoCE>0L){
                formulario.setIdFormato(idFormatoCE);
                formatoService.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            }else{
                formatoService.save(formulario);
                Toast.showToast(stage, "Se guardo correctamente!!", 2000, with, h);
                clearForm();



        }
    }}
    @FXML
    public void actualizarFormato() {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();

        if (formatoSeleccionado != null) {
            formatoSeleccionado.setNombre(txtNombreFormato.getText());
            formatoSeleccionado.setDescripcion(txtDescripcion.getText());
            formatoService.update(formatoSeleccionado); // Actualiza el formato
            listar(); // Actualiza la tabla
            clearForm(); // Limpia los campos
        } else {
            System.out.println("Selecciona un formato para actualizar.");
        }
    }

    @FXML
    public void eliminarFormato() {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();

        if (formatoSeleccionado != null) {
            formatoService.delete(formatoSeleccionado.getIdFormato()); // Elimina el formato
            listar(); // Actualiza la tabla
        } else {
            System.out.println("Selecciona un formato para eliminar.");
        }
    }


}
