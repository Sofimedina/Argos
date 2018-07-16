package Controladores;

import Api.ArgosInterface;
import Modelos.Animal;
import Modelos.Animales;
import Modelos.Usuario;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.InfoTagReader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.rmi.runtime.Log;

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
    private ComboBox puertosComboBox;
    @FXML
    private Button cambiarPuertoButton;
    @FXML
    private Circle tagInfoCircle;
    @FXML
    private Label tagInfoLabel;
    @FXML
    private Circle resultadoCircle;
    @FXML
    private Label resultadoLabel;
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
    private TextField idArgosTextField;
    @FXML
    private Button ampliarInfoAnimalButton;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField dueñoTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private TextField direccionTextField;
    @FXML
    private TextField cordenadasTextField;
    @FXML
    private Button leetTagButton;
    @FXML
    private Button editarTagButton;
    @FXML
    private Button guardarTagButton;
    @FXML
    private Button borrarTagButton;
    private String idAnimal=null;
    private ArgosInterface mApiInterface;
    private List<Animal> listaAnimales=null;
    private ObservableList<String> animalesObservableList=FXCollections.observableArrayList();
    private Usuario usuario=SesionInfo.getUsuarioLogueado();

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
        puertoLabel.setText("Puerto:"+InfoTagReader.getPuerto());
        puertosComboBox.setItems(obtenerListaPuertos());
        //Api client instancia
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        //Llenar lista de animales
        getListaAnimales();


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
            idAnimal=i.toString();
            displayAnimalSeleccionado(i);
            //enableButtonsAnimalSeleccionado();
        }else{
            //disableButtonsNingunAnimalSeleccionado();
        }

    }

    //Metodo para poner la info del animal seleccionado en display
    private void displayAnimalSeleccionado(Integer i){
        Animal animalSeleccionado=listaAnimales.get(i);
        idArgosTextField.setText(animalSeleccionado.getIdAnimal());
        nombreTextField.setText(animalSeleccionado.getNombre());
        if (usuario.getNombre()!=null){
            dueñoTextField.setText(usuario.getNombre());
        }
        if (usuario.getEmail()!=null){
            emailTextField.setText(usuario.getEmail());
        }
        if (usuario.getTelefonoFijo()!=null){
            telefonoTextField.setText(usuario.getTelefonoFijo());
        }
        if (usuario.getDireccionIndicaciones()!=null){
            direccionTextField.setText(usuario.getDireccionIndicaciones());
        }
        if (usuario.getCordenadasMaps()!=null){
            cordenadasTextField.setText(usuario.getCordenadasMaps());
        }
    }



    //Metodo para cargar lista de puertos
    private ObservableList<String> obtenerListaPuertos(){
        ObservableList<String> listaPuertos=FXCollections.observableArrayList("COM3","COM2","COM0");
        return listaPuertos;
    }


    //Metodo para modificar el puerto de conexion
    public void cambiarPuerto(ActionEvent actionEvent) {
        if (!puertosComboBox.getSelectionModel().isEmpty()){
            InfoTagReader.setPuerto(puertosComboBox.getValue().toString());
            puertoLabel.setText("Puerto:"+InfoTagReader.getPuerto());
        }
    }

    public void leetTag(ActionEvent actionEvent) {
        idAnimal="11";
        idArgosTextField.setText(idAnimal);
    }

    public void editarTag(ActionEvent actionEvent) {
    }

    public void guardarTag(ActionEvent actionEvent) {
    }

    public void eliminarTag(ActionEvent actionEvent) {
    }

    //Metodo para salir, volviendo al login
    public void volverAlLogin(ActionEvent actionEvent) throws IOException {
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
        if (!idArgosTextField.getText().isEmpty()){
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
