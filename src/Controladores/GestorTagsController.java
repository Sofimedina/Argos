package Controladores;

import Api.ArgosInterface;
import Modelos.Animal;
import Modelos.Animales;
import Modelos.TagInfo;
import Modelos.Usuario;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.SolverCodigosArduino;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestorTagsController implements Initializable {
    @FXML
    private Circle estadoArduinoCircle;
    @FXML
    private Label estadoArduinoLabel;
    @FXML
    private Label puertoLabel;
    @FXML
    private Button desconectarButton;
    @FXML
    private ComboBox puertosComboBox;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuItem administrarAnimalesMenuItem;
    @FXML
    private MenuItem modificarPerfiItem;
    @FXML
    private MenuItem aboutArgosItem;
    @FXML
    private MenuItem salirItem;
    @FXML
    private ListView animalesListView;
    @FXML
    private TextField idAnimalTextField;
    @FXML
    private Button ampliarInfoAnimalButton;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private TextField celularTextField;
    @FXML
    private Button leerTagButton;
    @FXML
    private Button escribirTagButton;
    @FXML
    private Button borrarTagButton;
    @FXML
    private Button resetearTagButton;
    @FXML
    private Button formatearTagButton;
    @FXML
    private Button getTagTypeButton;
    private String idAnimal=null;
    private ArgosInterface mApiInterface;
    private List<Animal> listaAnimales=null;
    private ObservableList<String> animalesObservableList=FXCollections.observableArrayList();
    private Usuario usuario=SesionInfo.getUsuarioLogueado();
    static SerialPort serialPort;
    String puertoSeleccionado;
    String respuestaArduino;

    //Instancia del controlador para enviar datos
    private static GestorTagsController instance;
    //Setear instancia del controlador
    public GestorTagsController(){
        instance=this;
    }
    //Obtener instancia del controlador
    public static GestorTagsController getInstance(){
        return instance;
    }
    //Obtener id Animal
    public String getIdAnimal(){
        return idAnimal;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Api client instancia
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        //Llenar lista de animales
        getListaAnimales();
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

    //Deshabilitar acciones posibles
    private void disableAcciones(){
        leerTagButton.setDisable(true);
        escribirTagButton.setDisable(true);
        borrarTagButton.setDisable(true);
        resetearTagButton.setDisable(true);
        formatearTagButton.setDisable(true);
        getTagTypeButton.setDisable(true);
    }
    //Habilitar acciones posibles
    private void enableAcciones(){
        leerTagButton.setDisable(false);
        escribirTagButton.setDisable(false);
        borrarTagButton.setDisable(false);
        resetearTagButton.setDisable(false);
        formatearTagButton.setDisable(false);
        getTagTypeButton.setDisable(false);
    }


    //Metodo para obtener lista de animales
    private void getListaAnimales(){
        listaAnimales=null;
        animalesListView.setDisable(true);
        animalesListView.getItems().clear();
        Call<Animales> getAnimalesCall=mApiInterface.getAnimales(SesionInfo.getClaveUsuario());
        getAnimalesCall.enqueue(new Callback<Animales>() {
            @Override
            public void onResponse(Call<Animales> call, Response<Animales> response) {
                Runnable runnableOnResponse=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()){
                                listaAnimales=response.body().getAnimales();
                                for (int i=0;i<listaAnimales.size();i++){
                                    animalesObservableList.add(i,listaAnimales.get(i).getNombre());
                                }
                                animalesListView.setItems(animalesObservableList);
                                animalesListView.setDisable(false);
                            }else{
                                animalesListView.setDisable(true);
                            }
                        }
                    });

                };
                Thread thread=new Thread(runnableOnResponse);
                thread.start();
            }

            @Override
            public void onFailure(Call<Animales> call, Throwable throwable) {
                Runnable runnableOnFailure=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            animalesListView.setDisable(true);
                        }
                    });

                };
                Thread thread=new Thread(runnableOnFailure);
                thread.start();

            }
        });
    }

    //Listener para cuando se selecciona un animal
    public void animalSeleccionado(MouseEvent mouseEvent) {
        Integer i=animalesListView.getSelectionModel().getSelectedIndex();
        if (i>=0) {
            //idAnimal=i.toString();
            displayAnimalSeleccionado(i);
        }

    }

    //Metodo para poner la info del animal seleccionado en display
    private void displayAnimalSeleccionado(Integer i){
        Animal animalSeleccionado=listaAnimales.get(i);
        idAnimalTextField.setText(animalSeleccionado.getIdAnimal());
        if (usuario.getTelefonoFijo()!=null){
            telefonoTextField.setText(usuario.getTelefonoFijo());
        }
        if (usuario.getTelefonoCelular()!=null){
            celularTextField.setText(usuario.getTelefonoCelular());
        }
    }

    //Intentar desconectar puerto
    public void intentarDesconectar(ActionEvent actionEvent) {
        puertosComboBox.getSelectionModel().clearSelection();
    }
    //Intentar leer tag
    public void intentarLeerTag(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        resetInputs();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task leerTask=leetTagTask();
        leerTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (leerTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
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
    //Intentar escribir tag
    public void intentarEscribirTag(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        String jsonString="w"+getJsonString();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task escribirTask=escribirTagTask(jsonString);
        escribirTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (escribirTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
                    if (escribirTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                            resetInputs();
                        }
                    }else {
                        mostrarRespuestas("Error escribiendo el tag");
                    }
                }
            }
        });
        new Thread(escribirTask).start();
    }
    //Intentar formatear tag
    public void intentarFormatearTag(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task formatearTask=formatearTagTask();
        formatearTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (formatearTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
                    if (formatearTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                        }
                    }else{
                        mostrarRespuestas("Error formateando el tag");
                    }
                }

            }
        });
        new Thread(formatearTask).start();
    }
    //Intentar borrar tag
    public void intentarBorrarTag(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task borrarTask=borrarTagTask();
        borrarTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (borrarTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
                    if (borrarTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            resetInputs();
                            mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                        }

                    }else{
                        mostrarRespuestas("Error borrando el tag");
                    }

                }
            }
        });
        new Thread(borrarTask).start();
    }
    //Intentar resetear tag
    public void intentarResetearTag(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task resetearTask=resetearTagTask();
        resetearTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (resetearTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
                    if (resetearTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            resetInputs();
                            mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                        }
                    }else {
                        mostrarRespuestas("Error reseteando el tag");
                    }
                }
            }
        });
        new Thread(resetearTask).start();
    }
    //Intentar ver si el tag es un miffare classic
    public void intentatGetTagType(ActionEvent actionEvent) {
        animalesListView.getSelectionModel().clearSelection();
        respuestaArduino=null;
        progressIndicator.visibleProperty().setValue(true);
        disableAcciones();
        Task getTypeTask=getTagTypeTask();
        getTypeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (getTypeTask.isDone()){
                    progressIndicator.visibleProperty().setValue(false);
                    enableAcciones();
                    if (getTypeTask.getValue().equals(true)){
                        if (respuestaArduino!=null){
                            mostrarRespuestas(SolverCodigosArduino.traducirRespuestaArduino(respuestaArduino));
                        }
                    }else{
                        mostrarRespuestas("Error obteniendo el tipo del tag");
                    }
                }

            }
        });
        new Thread(getTypeTask).start();
    }
    //Preparar json a guardar en el tag
    private String getJsonString(){
        //Base del json:{"idAnimal":"","telefonoFijo":"","telefonoCelular":"","e":""}
        //Numero de caracteres usado base=61;
        //Tamaño fijo del json a guardar en el tag
        int MAX_LENGHT=33;
        int characterUsed=0;
        int characterLeft=MAX_LENGHT-characterUsed;
        String jsonString="";

        if (idAnimalTextField.getText().length()>0){
            if (idAnimalTextField.getText().length()<=characterLeft){
                jsonString=jsonString+"{\"idAnimal\":\""+idAnimalTextField.getText()+"\",";
                characterUsed=characterUsed+idAnimalTextField.getText().length();
            }else{
                jsonString=jsonString+"{\"idAnimal\":\"\",";
            }
        }else{
            jsonString=jsonString+"{\"idAnimal\":\"\",";
        }
        //Calcular caracteres que quedan para usar
        characterLeft=MAX_LENGHT-characterUsed;


        if(telefonoTextField.getText().length()>0){
            if (telefonoTextField.getText().length()<=characterLeft){
                jsonString=jsonString+"\"telefonoFijo\":\""+telefonoTextField.getText()+"\",";
                characterUsed=characterUsed+telefonoTextField.getText().length();
            }else{
                jsonString=jsonString+"\"telefonoFijo\":\"\",";
            }

        }else{
            jsonString=jsonString+"\"telefonoFijo\":\"\",";

        }
        //Calcular caracteres que quedan para usar
        characterLeft=MAX_LENGHT-characterUsed;

        if (celularTextField.getText().length()>0){
            if (celularTextField.getText().length()<=characterLeft){
                jsonString=jsonString+"\"telefonoCelular\":\""+celularTextField.getText()+"\",";
                characterUsed=characterUsed+celularTextField.getText().length();
            }else{
                jsonString=jsonString+"\"telefonoCelular\":\"\",";
            }
        }else{
            jsonString=jsonString+"\"telefonoCelular\":\"\",";
        }

        //Calcular caracteres que quedan para usar
        characterLeft=MAX_LENGHT-characterUsed;

        if (characterLeft>0){
            jsonString=jsonString+"\"e\":\"";
            for (int i=0;i<characterLeft;i++){
                jsonString=jsonString+"0";
                characterUsed++;
            }
            jsonString=jsonString+"\"}";
        }else{
            jsonString=jsonString+"\"e\":\"\"}";
        }

        return jsonString;
    }

    //Setear estados para cuando el arduino este conectado
    private void estadoArduinoConectado(){
        desconectarButton.setDisable(false);
        enableAcciones();
        estadoArduinoCircle.setFill(Color.DARKCYAN);
        estadoArduinoLabel.setText("Estado:CONECTADO");
        puertoLabel.setText("Puerto:"+puertoSeleccionado);
    }
    //Setear estados para cuando el arduino este desconectado
    private void estadoArduinoDesconectado(){
        desconectarButton.setDisable(true);
        disableAcciones();
        estadoArduinoCircle.setFill(Color.GREY);
        estadoArduinoLabel.setText("Estado:DESCONECTADO");
        puertoLabel.setText("Puerto:");
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
    //Tarea para escribir un tag
    public Task<Boolean> escribirTagTask(String json){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try {
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    String command="w";
                    serialPort.writeString(json);
                    buffer = serialPort.readBytes(2,5000);
                    respuestaArduino=new String(buffer);
                    return true;
                }catch (SerialPortException | SerialPortTimeoutException e){
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }


    //Tarea para borrar un tag
    public Task<Boolean> borrarTagTask(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try{
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    serialPort.writeString("d");
                    buffer = serialPort.readBytes(2,5000);
                    respuestaArduino=new String(buffer);
                    return true;
                } catch (SerialPortException | SerialPortTimeoutException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        };
    }

    //Tarea para resetear un tag
    public Task<Boolean> resetearTagTask(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try{
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    serialPort.writeString("c");
                    buffer = serialPort.readBytes(2,5000);
                    respuestaArduino=new String(buffer);
                    return true;
                } catch (SerialPortException | SerialPortTimeoutException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        };
    }

    //Tarea para formatear un tag
    public Task<Boolean> formatearTagTask(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try{
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    serialPort.writeString("f");
                    buffer = serialPort.readBytes(2,5000);
                    respuestaArduino=new String(buffer);
                    return true;
                } catch (SerialPortException | SerialPortTimeoutException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        };
    }

    //Tarea para determinar si un tag es mifare classic o no
    public Task<Boolean> getTagTypeTask(){
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                byte[] buffer = null;
                try{
                    serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
                    serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
                    serialPort.writeString("t");
                    buffer = serialPort.readBytes(2,5000);
                    respuestaArduino=new String(buffer);
                    return true;
                } catch (SerialPortException | SerialPortTimeoutException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        };
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

    //Metodo para salir, volviendo al login
    public void volverAlLogin(ActionEvent actionEvent) throws IOException {
        cerrarPuerto();
        SesionInfo.desconectarUsuario();
        menuButton.getScene().getWindow().hide();
        Parent parentLogin=FXMLLoader.load(getClass().getResource("/Vistas/LoginView.fxml"));
        Scene LoginScene=new Scene(parentLogin);
        Stage LoginStage=new Stage();
        LoginStage.setResizable(false);
        LoginStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        LoginStage.setScene(LoginScene);
        LoginStage.setTitle("Argos - Login");
        LoginStage.show();
    }


    //Abrir view para ampliar la info de un animal
    public void launchInfoAnimalView(ActionEvent actionEvent) throws IOException {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (!idAnimalTextField.getText().isEmpty()){
            idAnimal=idAnimalTextField.getText();
            cerrarPuerto();
            ampliarInfoAnimalButton.getScene().getWindow().hide();
            Parent parentInfoAnimal=FXMLLoader.load(getClass().getResource("/Vistas/InfoAnimalView.fxml"));
            Scene InfoAnimalScene=new Scene(parentInfoAnimal);
            Stage InfoAnimalStage=new Stage();
            InfoAnimalStage.setResizable(false);
            InfoAnimalStage.getIcons().add(new Image("/Imgs/IconV32.png"));
            InfoAnimalStage.setScene(InfoAnimalScene);
            InfoAnimalStage.setTitle("Argos - Informacion del animal");
            InfoAnimalStage.show();
        }else{
            alert.setContentText("No hay id para leer");
            alert.show();
        }
    }

    //Mostrar view de administracion de animales
    public void launchAdministrarAnimalesView(ActionEvent actionEvent) throws IOException {
        cerrarPuerto();
        menuButton.getScene().getWindow().hide();
        Parent parentAdministrarAnimales=FXMLLoader.load(getClass().getResource("/Vistas/AdministrarAnimalesView.fxml"));
        Scene AdministrarAnimalesScene=new Scene(parentAdministrarAnimales);
        Stage AdministrarAnimalesStage=new Stage();
        AdministrarAnimalesStage.setResizable(false);
        AdministrarAnimalesStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AdministrarAnimalesStage.setScene(AdministrarAnimalesScene);
        AdministrarAnimalesStage.setTitle("Argos - Administrar animales");
        AdministrarAnimalesStage.show();
    }

    //Mostrar view de la administracion del perfil de usuario
    public void launchAdministrarPerfilView(ActionEvent actionEvent) throws IOException {
        cerrarPuerto();
        menuButton.getScene().getWindow().hide();
        Parent parentAdministrarPerfil=FXMLLoader.load(getClass().getResource("/Vistas/AdministrarPerfilView.fxml"));
        Scene AdministrarPerfilScene=new Scene(parentAdministrarPerfil);
        Stage AdministrarPerfilStage=new Stage();
        AdministrarPerfilStage.setResizable(false);
        AdministrarPerfilStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AdministrarPerfilStage.setScene(AdministrarPerfilScene);
        AdministrarPerfilStage.setTitle("Argos - Administrar perfil");
        AdministrarPerfilStage.show();

    }

    //Mostrar view ArgosInfo
    public void launchAcercaDeArgos(ActionEvent actionEvent) throws IOException {
        cerrarPuerto();
        menuButton.getScene().getWindow().hide();
        Parent parentAcercaDeArgos=FXMLLoader.load(getClass().getResource("/Vistas/AcercaDeArgosView.fxml"));
        Scene AcercaDeArgosScene=new Scene(parentAcercaDeArgos);
        Stage AcercaDeArgosStage=new Stage();
        AcercaDeArgosStage.setResizable(false);
        AcercaDeArgosStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AcercaDeArgosStage.setScene(AcercaDeArgosScene);
        AcercaDeArgosStage.setTitle("Acerca de Argos");
        AcercaDeArgosStage.show();
    }



}
