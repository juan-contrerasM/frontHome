

package com.example.homeview;

import com.example.homeview.model.Pyp;
import com.example.homeview.servi.ServiAPiPyp;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;

public class ControllerPyp {

    @FXML
    private TableColumn<Pyp, Integer> columCantRecuperaciones;

    @FXML
    private TableColumn<Pyp, String> columCedula;

    @FXML
    private TableColumn<Pyp, Long> columId;

    @FXML
    private TableColumn<Pyp, String> columNombre;

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
    private TableView<Pyp> tablePYP;

    @FXML
    private TextField txtBuscador;

    @FXML
    private TextField txtPypCol;

    @FXML
    private TextField txtcantRecuperaciones;

    @FXML
    private TextField txtcedula;
    @FXML
    private TableColumn<Pyp, String> columPypCol;
    private ServiAPiPyp serviAPiPyp;
    private Gson gson;
    private Long idSeleccion;


    @FXML
    private TextField txtnombre;
    // Lista observable para almacenar los datos
    private ObservableList<Pyp> listaPyp;

    @FXML
    public void initialize() throws Exception {
        gson= new Gson();
        serviAPiPyp =new ServiAPiPyp();
        // Inicializar la lista observable
        listaPyp = FXCollections.observableArrayList();

        // Configurar las columnas de la tabla
        columId.setCellValueFactory(new PropertyValueFactory<>("id_pyp"));
        columCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columCantRecuperaciones.setCellValueFactory(new PropertyValueFactory<>("cantida_recuperaciones"));
        columPypCol.setCellValueFactory(new  PropertyValueFactory<>("pypcol"));
        // Vincular la lista observable a la tabla
        tablePYP.setItems(listaPyp);

        // Cargar datos iniciales
        cargarDatos();
        configurarSeleccionTabla();
    }

    public void cargarDatos() throws Exception {
        listaPyp.addAll(serviAPiPyp.obtenerPyps());
    }

    @FXML
    void actualizar(MouseEvent event) throws Exception {
        if(verificarCampos()){
            Pyp pyp = new Pyp();
            pyp.setCedula(txtnombre.getText());
            pyp.setNombre(txtnombre.getText());
            pyp.setCantida_recuperaciones(Integer.valueOf(txtcantRecuperaciones.getText()));
            pyp.setPypcol(txtPypCol.getText());
            String jsonPyp = crearJson(pyp);
            serviAPiPyp.actualizarPyp(jsonPyp,idSeleccion);
            limpiarCampos();
            idSeleccion=null;
        }

    }

    @FXML
    void buscar(MouseEvent event) throws Exception {
        if(!txtBuscador.getText().isEmpty()) {
            Pyp pyp = serviAPiPyp.obtenerPyp(Long.valueOf(txtBuscador.getText()));
            listaPyp.clear();
            listaPyp.add(pyp);
            txtBuscador.setText("");
        }else {
            mostrarAlerta(Alert.AlertType.WARNING,"el buscador esta vacio","no existe un pyp que tenga el id vacio");
        }
    }

    @FXML
    void crear(MouseEvent event) throws Exception {
        if(verificarCampos()) {
            Pyp pyp = new Pyp();
            pyp.setCedula(txtnombre.getText());
            pyp.setNombre(txtnombre.getText());
            pyp.setCantida_recuperaciones(Integer.valueOf(txtcantRecuperaciones.getText()));
            pyp.setPypcol(txtPypCol.getText());
            String jsonPyp = crearJson(pyp);
            serviAPiPyp.guardarPyp(jsonPyp);
            limpiarCampos();
        }else {
            mostrarAlerta(Alert.AlertType.WARNING,"Faltan campor por llenar","Debe llenar todos los campos");
        }
    }

    public  void limpiarCampos(){
        txtnombre.setText("");
        txtPypCol.setText("");
        txtcedula.setText("");
        txtcantRecuperaciones.setText("");
    }
    public boolean verificarCampos(){
        boolean confirmacion = true;
        if(txtnombre.getText().isEmpty()||txtnombre.getText()==null ){
            confirmacion=false;
        } else if (txtPypCol.getText().isEmpty()||txtPypCol.getText()==null) {
            confirmacion=false;

        } else if (txtcantRecuperaciones.getText().isEmpty()||txtcantRecuperaciones.getText()==null) {
            confirmacion=false;
        } else if (txtcedula.getText().isEmpty()||txtcedula.getText()==null) {
            confirmacion= false;
        }
        return confirmacion;
    }
    // Método para convertir un solo objeto Pyp a JSON
    public String crearJson(Pyp pyp) {
        return gson.toJson(pyp);
    }

    @FXML
    void eliminar(MouseEvent event) throws Exception {
        if(idSeleccion!= null) {
            if (mostrarConfirmacion("Desea Eliminar el pyp con el id: " + idSeleccion)) {
                serviAPiPyp.eliminarPyp(idSeleccion);
            }
        }
        limpiarCampos();



    }
    public boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }


    @FXML
    void recargar(MouseEvent event) throws Exception {
        listaPyp.clear();
        listaPyp.addAll(serviAPiPyp.obtenerPyps());
        limpiarCampos();

    }
    public  void mostrarAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Elimina el encabezado
        alert.setContentText(message);
        alert.showAndWait(); // Muestra la alerta y espera hasta que el usuario la cierre
    }
    private void configurarSeleccionTabla() {
        // Permitir selección única
        tablePYP.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Listener para detectar selección
        tablePYP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Pyp>() {
            @Override
            public void changed(ObservableValue<? extends Pyp> observable, Pyp oldValue, Pyp newValue) {
                if (newValue != null) {
                    mostrarDatosSeleccionados(newValue);
                }
            }
        });
    }

    private void mostrarDatosSeleccionados(Pyp newValue) {
        txtcedula.setText(newValue.getCedula());
        txtnombre.setText(newValue.getNombre());
        txtcantRecuperaciones.setText(String.valueOf(newValue.getCantida_recuperaciones()));
        txtPypCol.setText(newValue.getPypcol());
        idSeleccion=newValue.getId_pyp();
    }

}
