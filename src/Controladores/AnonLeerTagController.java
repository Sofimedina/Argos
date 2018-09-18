package Controladores;

import Modelos.TagInfo;
import Utilidades.SolverCodigosArduino;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AnonLeerTagController implements Initializable {

    @FXML
    private Circle estadoArduinoCircle;
    @FXML
    private Label estadoArduinoLabel;
    @FXML
    private Label puertoArduinoLabel;
    @FXML
    private ComboBox puertosComboBox;
    @FXML
    private Button desconectarButton;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextField idAnimalTextField;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private TextField celularTextField;
    @FXML
    private Button leerTagButton;
    @FXML
    private Button volverAlLoginButton;
    static SerialPort serialPort;
    String puertoSeleccionado;
    String respuestaArduino;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        estadoArduinoDesconectado();
        puertosComboBox.setDisable(true);
        String[] portNames= SerialPortList.getPortNames();
        if (portNames.length>0){
            puertosComboBox.setDisable(false);
            ObservableList<String> listaPuertos= FXCollections.observableArrayList(portNames);
            puertosComboBox.setItems(listaPuertos);
        }
        puertosComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue!=null){
                    puertoSeleccionado=puertosComboBox.getSelectionModel().getSelectedItem().toString();
                    Task conectarArduinoTask=conectarArduino();
                    progressIndicator.visibleProperty().setValue(true);
                    conectarArduinoTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            if (conectarArduinoTask.isDone()){
                                progressIndicator.visibleProperty().setValue(false);
                                if (conectarArduinoTask.getValue().equals(true)){
                                    if (conectarArduinoTask.getValue().equals(true)){
                                        estadoArduinoConectado();
                                    }
                                }else {
                                    mostrarRespuestas("Error al conectar el dispositivo");
                                    puertosComboBox.getSelectionModel().clearSelection();
                                    estadoArduinoDesconectado();
                                }

                            }
                        }
                    });
                    new Thread(conectarArduinoTask).start();
                }else{
                    if (cerrarPuerto()){
                        puertoSeleccionado=null;
                        estadoArduinoDesconectado();
                        resetInputs();
                    }
                }
            }
        });
    }
    //Intentar leer tag
    public void leerTag(ActionEvent actionEvent) {
        resetInputs();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        leerTagButton.setDisable(true);
        Task leerTask=leetTagTask();
        leerTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (leerTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    leerTagButton.setDisable(false);
                    if (leerTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            if (!respuestaArduino.equals(SolverCodigosArduino.E_READING_NO_TAG_C)&&
                                    !respuestaArduino.equals(SolverCodigosArduino.E_T_NO_INFO_C)&&
                                    !respuestaArduino.equals(SolverCodigosArduino.E_T_EMPTY_C)){
                                TagInfo tagInfo=getJsonRespuesta(respuestaArduino);
                                if (tagInfo!=null){
                                    if (tagInfo.getIdAnimal()!=null&&tagInfo.getIdAnimal().length()>0){
                                        idAnimalTextField.setText(tagInfo.getIdAnimal());
                                    }
                                    if (tagInfo.getTelefonoCelular()!=null&&tagInfo.getTelefonoCelular().length()>0){
                                        celularTextField.setText(tagInfo.getTelefonoCelular());
                                    }
                                    if (tagInfo.getTelefonoFijo()!=null&&tagInfo.getTelefonoFijo().length()>0){
                                        telefonoTextField.setText(tagInfo.getTelefonoFijo());
                                    }
                                }else{
                                    mostrarRespuestas("El tag posee un formato incorrecto");
                                }
                            }else {
                                mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                            }
                        }
                    }else{
                        mostrarRespuestas("Error leyendo el tag");
                    }

                }
            }
        });
        new Thread(leerTask).start();
    }


    //Desconectar arduino
    public void intentarDesconectar(ActionEvent actionEvent) {
        puertosComboBox.getSelectionModel().clearSelection();
    }

    //Setear estados para cuando el arduino este conectado
    private void estadoArduinoConectado(){
        desconectarButton.setDisable(false);
        leerTagButton.setDisable(false);
        estadoArduinoCircle.setFill(Color.DARKCYAN);
        estadoArduinoLabel.setText("Estado:CONECTADO");
        puertoArduinoLabel.setText("Puerto:"+puertoSeleccionado);
    }
    //Setear estados para cuando el arduino este desconectado
    private void estadoArduinoDesconectado(){
        desconectarButton.setDisable(true);
        leerTagButton.setDisable(true);
        estadoArduinoCircle.setFill(Color.GREY);
        estadoArduinoLabel.setText("Estado:DESCONECTADO");
        puertoArduinoLabel.setText("Puerto:");
    }

    //Tarea para conectar al arduino,se agrega una espera para asegurar la comunicacion con el board
    public Task<Boolean> conectarArduino(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try{
                    serialPort=new jssc.SerialPort(puertoSeleccionado);
                    serialPort.openPort();
                    serialPort.setParams(jssc.SerialPort.BAUDRATE_9600,
                            jssc.SerialPort.DATABITS_8,
                            jssc.SerialPort.STOPBITS_1,
                            jssc.SerialPort.PARITY_NONE);
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }finally {
                    if (serialPort.isOpened()){
                        for (int i=0;i<5;i++){
                            Thread.sleep(1000);
                        }
                        return true;
                    }else {
                        return false;
                    }
                }
            }
        };
    }
    //Tarea para leer un tag
    public Task<Boolean> leetTagTask(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try{
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    serialPort.writeString("r");
                    buffer = serialPort.readBytes(94, 5000);
                    respuestaArduino=new String(buffer);
                    return true;
                } catch (SerialPortException | SerialPortTimeoutException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        };
    }
    //Intentar cerrar el puerto conectado
    public Boolean cerrarPuerto(){
        Boolean b=false;
        if (serialPort!=null){
            if (serialPort.isOpened()) {
                try {
                    serialPort.closePort();
                    b = true;
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }
    //Interpretar el json obtenido del tag
    private TagInfo getJsonRespuesta(String jsonString){
        TagInfo tagInfo=null;
        try{
            Gson gson=new Gson();
            tagInfo=gson.fromJson(jsonString,TagInfo.class);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return tagInfo;
    }

    //Resetear informacion de los inputs
    private void resetInputs(){
        idAnimalTextField.setText("");
        telefonoTextField.setText("");
        celularTextField.setText("");
    }

    //Mostrar un dialogo para comunicar resultados y errores
    public static  void mostrarRespuestas(String respuesta){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        alert.setContentText(respuesta);
        alert.show();
    }

    //Metodo para volver a la vista del login
    public void launchLoginView(ActionEvent actionEvent) throws IOException {
        cerrarPuerto();
        volverAlLoginButton.getScene().getWindow().hide();
        Parent parentLogin=FXMLLoader.load(getClass().getResource("/Vistas/LoginView.fxml"));
        Scene LoginScene=new Scene(parentLogin);
        Stage LoginStage=new Stage();
        LoginStage.setResizable(false);
        LoginStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        LoginStage.setScene(LoginScene);
        LoginStage.setTitle("Argos - Login");
        LoginStage.show();
    }
}
