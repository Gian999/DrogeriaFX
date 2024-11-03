package pe.edu.upeu.drogeriafx.control;

import jakarta.validation.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.drogeriafx.componente.ColumnInfo;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.modelo.Producto;
import pe.edu.upeu.drogeriafx.servicio.FormatoService;
import pe.edu.upeu.drogeriafx.componente.TableViewHelper;
import pe.edu.upeu.drogeriafx.componente.Toast;

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
    private TableView<Formato> formatoTable;
    @FXML
    Label lbnMsg;
    @FXML
    private VBox miContenedor;
    Stage stage;

    @Autowired
    FormatoService fs;

    private Validator validator;
    ObservableList<Formato> listarFormato;
    Formato formulario;
    Long idFormatoCE = 0L;

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
            fs.delete(formato.getIdFormato());
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(formatoTable, columns, updateAction, deleteAction);

        formatoTable.setTableMenuButtonVisible(true);
        listar();
    }

    @FXML
    public void listar() {
        try {
            formatoTable.getItems().clear();
            listarFormato = FXCollections.observableArrayList(fs.list());
            formatoTable.getItems().addAll(listarFormato);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void limpiarError() {
        txtNombreFormato.getStyleClass().remove("text-field-error");
        txtDescripcion.getStyleClass().remove("text-field-error");
    }

    @FXML
    public void clearForm() {
        txtNombreFormato.setText("");
        txtDescripcion.setText("");
        idFormatoCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
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
            }
        }
        String mensajeError = erroresOrdenados.values().stream().collect(Collectors.joining("\n"));
        lbnMsg.setText(mensajeError);
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }



    @FXML
    public void actualizarFormato() {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();

        if (formatoSeleccionado != null) {
            formatoSeleccionado.setNombre(txtNombreFormato.getText());
            formatoSeleccionado.setDescripcion(txtDescripcion.getText());
            fs.update(formatoSeleccionado); // Actualiza el formato
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
            fs.delete(formatoSeleccionado.getIdFormato()); // Elimina el formato
            listar(); // Actualiza la tabla
        } else {
            System.out.println("Selecciona un formato para eliminar.");
        }
    }



    @FXML
    public void validarFormulario() {
        String nombre = txtNombreFormato.getText();
        String descripcion = txtDescripcion.getText();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            try {
                Formato nuevoFormato = new Formato();
                nuevoFormato.setNombre(nombre);
                nuevoFormato.setDescripcion(descripcion);

                double with = stage.getWidth() / 1.5;
                double h = stage.getHeight() / 2;

                if (idFormatoCE != 0L && idFormatoCE > 0L) {
                    nuevoFormato.setIdFormato(idFormatoCE);
                    fs.update(nuevoFormato);
                    Toast.showToast(stage, "Se actualizó correctamente!", 2000, with, h);
                } else {
                    fs.save(nuevoFormato);
                    Toast.showToast(stage, "Se guardó correctamente!", 2000, with, h);
                }

                clearForm();
                listar();
                lbnMsg.setText("Formulario válido");
                lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                limpiarError();
            } catch (ConstraintViolationException e) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                    sb.append(violation.getMessage()).append("\n");
                }
                lbnMsg.setText("Error al guardar: " + sb.toString());
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            } catch (Exception e) {
                e.printStackTrace();
                lbnMsg.setText("Error al guardar: " + e.getMessage());
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            }
        } else {
            lbnMsg.setText("Por favor, complete todos los campos");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }


    public void editForm(Formato formato){
        txtNombreFormato.setText(formato.getNombre());
        txtDescripcion.setText(formato.getDescripcion());
        idFormatoCE=formato.getIdFormato();
        limpiarError();
    }







}
