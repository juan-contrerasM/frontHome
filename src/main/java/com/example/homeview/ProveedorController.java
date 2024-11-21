package com.example.homeview;

import com.example.homeview.model.Proveedor;
import com.example.homeview.model.Pyp;
import com.example.homeview.servi.ServiProveedor;
import com.google.gson.Gson;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class ProveedorController implements Initializable {

    @FXML
    private TableColumn<Proveedor, String> columCedula;

    @FXML
    private TableColumn<Proveedor, Float> columDescuento;

    @FXML
    private TableColumn<Proveedor, String> columNombre;

    @FXML
    private TableColumn<Proveedor, String> columRUT;

    @FXML
    private ImageView imgActualizar;

    @FXML
    private ImageView imgBucar;

    @FXML
    private ImageView imgCrear;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private ImageView imgeRecargar;

    @FXML
    private TableView<Proveedor> tableProveedor;

    @FXML
    private TextField txtBuscador;

    @FXML
    private TextField txtDescuento;

    @FXML
    private TextField txtRUT;

    @FXML
    private TextField txtcedula;

    @FXML
    private TextField txtnombre;
    private ServiProveedor serviProveedor;
    private Gson gson;

    private ObservableList<Proveedor>listaProveedores;

    @FXML
    void actualizar(MouseEvent event) throws Exception {
        if(verificarCampos()) {
            Proveedor proveedor = new Proveedor();
            proveedor.setCedula(txtcedula.getText());
            proveedor.setDescuento(Float.parseFloat(txtDescuento.getText()));
            proveedor.setNombre(txtnombre.getText());
            proveedor.setRut(txtRUT.getText());
            String jsonProveedor = crearJson(proveedor);
            serviProveedor.actualizarproveedor(jsonProveedor,txtcedula.getText());
            limpiarCampos();

        }


    }

    @FXML
    void buscar(MouseEvent event) throws Exception {
        if(!txtBuscador.getText().isEmpty()) {
            Proveedor proveedor = serviProveedor.obtenerProveedor(txtBuscador.getText());
            listaProveedores.clear();
            listaProveedores.add(proveedor);
            txtBuscador.setText("");
        }else {
            mostrarAlerta(Alert.AlertType.WARNING,"el buscador esta vacio","no existe un proveedor que tenga el id vacio");
        }

    }

    @FXML
    void crear(MouseEvent event) throws Exception {
        if(verificarCampos()){
            Proveedor proveedor= new Proveedor();
            proveedor.setCedula(txtcedula.getText());
            proveedor.setDescuento(Float.parseFloat(txtDescuento.getText()));
            proveedor.setNombre(txtnombre.getText());
            proveedor.setRut(txtRUT.getText());
            String jsonProveedor= crearJson(proveedor);
            serviProveedor.guardarProveedor(jsonProveedor);
            limpiarCampos();
        }else {
            mostrarAlerta(Alert.AlertType.WARNING,"Faltan campor por llenar","Debe llenar todos los campos");
        }

    }
    public  void mostrarAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Elimina el encabezado
        alert.setContentText(message);
        alert.showAndWait(); // Muestra la alerta y espera hasta que el usuario la cierre
    }
    public String crearJson(Proveedor proveedor) {
        return gson.toJson(proveedor);
    }

    @FXML
    void eliminar(MouseEvent event) throws Exception {
        if(txtcedula.getText()!= null){
            if (mostrarConfirmacion("Desea Eliminar el pyp con el cedula: " + txtcedula.getText())) {
                serviProveedor.eliminarProveedor(txtcedula.getText());
            }
        }
        limpiarCampos();
    }

    @FXML
    void recargar(MouseEvent event) throws Exception {
        listaProveedores.clear();
        listaProveedores.addAll(serviProveedor.obtenerProveedores());
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
        gson= new Gson();
        serviProveedor = new ServiProveedor();
        listaProveedores= FXCollections.observableArrayList();

        columCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
        columRUT.setCellValueFactory(new PropertyValueFactory<>("rut"));

        tableProveedor.setItems(listaProveedores);

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
        tableProveedor.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Proveedor>() {
            @Override
            public void changed(ObservableValue<? extends Proveedor> observable, Proveedor oldValue, Proveedor newValue) {
                if (newValue != null) {
                    mostrarDatosSeleccionados(newValue);
                }
            }
        });
    }
    private void mostrarDatosSeleccionados(Proveedor newValue) {
        txtcedula.setText(newValue.getCedula());
        txtnombre.setText(newValue.getNombre());
        txtDescuento.setText(String.valueOf(newValue.getDescuento()));
        txtRUT.setText(newValue.getRut());
    }

    public void cargarDatos() throws Exception {
        listaProveedores.addAll(serviProveedor.obtenerProveedores());
    }
    public  void limpiarCampos() {
        txtRUT.setText("");
        txtDescuento.setText("");
        txtnombre.setText("");
        txtcedula.setText("");
    }

    public  boolean verificarCampos (){
        boolean confirmacion = true;
        if(txtnombre.getText().isEmpty()||txtnombre.getText()==null ){
            confirmacion=false;
        } else if (txtDescuento.getText().isEmpty()||txtDescuento.getText()==null) {
            confirmacion=false;

        } else if (txtRUT.getText().isEmpty()||txtRUT.getText()==null) {
            confirmacion=false;
        } else if (txtcedula.getText().isEmpty()||txtcedula.getText()==null) {
            confirmacion= false;
        }
        return confirmacion;
    }

}


