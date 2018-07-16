package Controladores;

import Prefs.SesionInfo;
import Utilidades.InfoTagReader;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
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
    private ComboBox listaAnimalesComboBox;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        puertoLabel.setText("Puerto:"+InfoTagReader.getPuerto());
        puertosComboBox.setItems(obtenerListaPuertos());
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
        idArgosTextField.setText("399393jdjd");
    }

    public void editarTag(ActionEvent actionEvent) {
    }

    public void guardarTag(ActionEvent actionEvent) {
    }

    public void eliminarTag(ActionEvent actionEvent) {
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
