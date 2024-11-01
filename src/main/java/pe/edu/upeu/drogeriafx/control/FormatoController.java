package pe.edu.upeu.drogeriafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.servicio.FormatoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FormatoController {

    @FXML
    private TableView<Formato> formatoTable;
    @FXML
    private TableColumn<Formato, Long> colId;
    @FXML
    private TableColumn<Formato, String> colNombre;
    @FXML
    private TableColumn<Formato, String> colDescripcion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;

    @Autowired
    private FormatoService formatoService;

    private ObservableList<Formato> listaFormatos;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFormato"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cargarDatos();
    }

    public void cargarDatos() {
        listaFormatos = FXCollections.observableArrayList(formatoService.obtenerTodos());
        formatoTable.setItems(listaFormatos);
    }

    @FXML
    private void agregarFormato() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarMensaje("Error", "Los campos no deben estar vacíos", Alert.AlertType.ERROR);
            return;
        }

        Formato nuevoFormato = new Formato();
        nuevoFormato.setNombre(nombre);
        nuevoFormato.setDescripcion(descripcion);
        formatoService.guardar(nuevoFormato);
        listaFormatos.add(nuevoFormato);
        limpiarCampos();
        mostrarMensaje("Éxito", "Formato agregado correctamente", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void eliminarFormato() {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();
        if (formatoSeleccionado == null) {
            mostrarMensaje("Error", "Debes seleccionar un formato para eliminar", Alert.AlertType.ERROR);
            return;
        }

        formatoService.eliminar(formatoSeleccionado.getIdFormato());
        listaFormatos.remove(formatoSeleccionado);
        mostrarMensaje("Éxito", "Formato eliminado correctamente", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void seleccionarFormato(MouseEvent event) {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();
        if (formatoSeleccionado != null) {
            txtNombre.setText(formatoSeleccionado.getNombre());
            txtDescripcion.setText(formatoSeleccionado.getDescripcion());
        }
    }

    @FXML
    private void actualizarFormato() {
        Formato formatoSeleccionado = formatoTable.getSelectionModel().getSelectedItem();
        if (formatoSeleccionado == null) {
            mostrarMensaje("Error", "Selecciona un formato para actualizar", Alert.AlertType.ERROR);
            return;
        }

        formatoSeleccionado.setNombre(txtNombre.getText());
        formatoSeleccionado.setDescripcion(txtDescripcion.getText());
        formatoService.guardar(formatoSeleccionado);  // Usamos el mismo método `guardar` para actualizar
        cargarDatos();
        limpiarCampos();
        mostrarMensaje("Éxito", "Formato actualizado correctamente", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDescripcion.clear();
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
