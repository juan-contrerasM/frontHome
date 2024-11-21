package com.example.homeview;

import com.example.homeview.model.Operador;
import com.example.homeview.servi.ServiOperador;
import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;


public class OperadorController implements Initializable {

    @FXML
    private TableColumn<Operador, String> columNombre;

    @FXML
    private ImageView imgBucar;

    @FXML
    private TableView<Operador> tableProveedor;

    @FXML
    private ImageView imgeRecargar;

    @FXML
    private TextField txtcedula;

    @FXML
    private TextField txtIndicadorAislamiento;

    @FXML
    private TableColumn<Operador, String> columCedula;

    @FXML
    private TextField txtBuscador;

    @FXML
    private ImageView imgCrear;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private TableColumn<Operador, String> columApellido;

    @FXML
    private TableColumn<Operador, Date> columFechaNacimiento;

    @FXML
    private TextField txtApellido;

    @FXML
    private TableColumn<Operador, String> columIndicadorAislamiento;

    @FXML
    private TextField txtnombre;

    @FXML
    private TextField txtFechaNacimiento;

    @FXML
    private ImageView imgActualizar;


//-----------------------------------------------------------------------------


    private ServiOperador serviOperador;
    private Gson gson;

    private ObservableList<Operador> listaOperadores;

    @FXML
    void actualizar(MouseEvent event) throws Exception {
        if (verificarCampos()) {
            Operador operador = new Operador();
            operador.setCedula(txtcedula.getText());
            operador.setNombre(txtnombre.getText());
            operador.setApellido(txtnombre.getText());
            operador.setFecha_nacimiento(Date.valueOf(txtnombre.getText()));
            operador.setIndicador_aislamiento(txtnombre.getText());
            //-----------

            String jsonProveedor = crearJson(operador);
            serviOperador.actualizarOperador(jsonProveedor, txtcedula.getText());
            limpiarCampos();
        }
    }

    @FXML
    void buscar(MouseEvent event) throws Exception {
        if (!txtBuscador.getText().isEmpty()) {
            Operador operador = serviOperador.obtenerOperador(txtBuscador.getText());
            listaOperadores.clear();
            listaOperadores.add(operador);
            txtBuscador.setText("");
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "el buscador esta vacio", "no existe un operador que tenga el id vacio");
        }
    }


    @FXML
    void crear(MouseEvent event) throws Exception {
        if (verificarCampos()) {
            Operador operador = new Operador();
            operador.setCedula(txtcedula.getText());
            operador.setNombre(txtnombre.getText());
            operador.setApellido(txtApellido.getText());
            java.util.Date fecha = Date.valueOf(txtFechaNacimiento.getText());
            System.out.println(fecha);
            operador.setFecha_nacimiento((Date) fecha);
            operador.setIndicador_aislamiento(txtIndicadorAislamiento.getText());

            String jsonProveedor = crearJson(operador);
            serviOperador.guardarOperador(jsonProveedor);
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan campor por llenar", "Debe llenar todos los campos");
        }

    }

    public void mostrarAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Elimina el encabezado
        alert.setContentText(message);
        alert.showAndWait(); // Muestra la alerta y espera hasta que el usuario la cierre
    }

    public String crearJson(Operador operador) {
        return gson.toJson(operador);
    }

    @FXML
    void eliminar(MouseEvent event) throws Exception {
        if (txtcedula.getText() != null) {
            if (mostrarConfirmacion("Desea Eliminar el operador con el cedula: " + txtcedula.getText())) {
                serviOperador.eliminarOperador(txtcedula.getText());
            }
        }
        limpiarCampos();
    }

    @FXML
    void recargar(MouseEvent event) throws Exception {
        listaOperadores.clear();
        listaOperadores.addAll(serviOperador.obtenerOperadores());
        limpiarCampos();

    }

    public boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gson = new Gson();
        serviOperador = new ServiOperador();
        listaOperadores = FXCollections.observableArrayList();

        columCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fecha_nacimiento"));
        columIndicadorAislamiento.setCellValueFactory(new PropertyValueFactory<>("indicador_aislamiento"));

        tableProveedor.setItems(listaOperadores);

        try {
            cargarDatos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        configurarSeleccionTabla();
    }


    private void configurarSeleccionTabla() {
        // Permitir selección única
        tableProveedor.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Listener para detectar selección
        tableProveedor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Operador>() {
            @Override
            public void changed(ObservableValue<? extends Operador> observable, Operador oldValue, Operador newValue) {
                if (newValue != null) {
                    mostrarDatosSeleccionados(newValue);
                }
            }
        });
    }

    private void mostrarDatosSeleccionados(Operador newValue) {
        txtcedula.setText(newValue.getCedula());
        txtnombre.setText(newValue.getNombre());
        txtApellido.setText(newValue.getApellido());
        txtFechaNacimiento.setText(String.valueOf(newValue.getFecha_nacimiento()));
        txtIndicadorAislamiento.setText(newValue.getIndicador_aislamiento());
    }

    public void cargarDatos() throws Exception {
        listaOperadores.addAll(serviOperador.obtenerOperadores());
    }

    public void limpiarCampos() {
        txtnombre.setText("");
        txtcedula.setText("");
        txtApellido.setText("");
        txtIndicadorAislamiento.setText("");
        txtFechaNacimiento.setText("");
    }

    public boolean verificarCampos() {
        boolean confirmacion = true;
        if (txtnombre.getText().isEmpty() || txtnombre.getText() == null) {
            confirmacion = false;
        } else if (txtApellido.getText().isEmpty() || txtApellido.getText() == null) {
            confirmacion = false;
        } else if (txtIndicadorAislamiento.getText().isEmpty() || txtIndicadorAislamiento.getText() == null) {
            confirmacion = false;
        } else if (txtcedula.getText().isEmpty() || txtcedula.getText() == null) {
            confirmacion = false;
        }
        return confirmacion;
    }

}


