package Controladores;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

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
    private Button cambiarPuertoButton;
    @FXML
    private Circle infoTagCircle;
    @FXML
    private Label infoTagLabel;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField dueñoTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField telefonoTextField;
    @FXML
    private TextField celularTextField;
    @FXML
    private TextField direccionTextField;
    @FXML
    private TextField cordenadasTextField;
    @FXML
    private Button leerTagButton;
    @FXML
    private Button volverAlLoginButton;


    public void leerTag(ActionEvent actionEvent) {
        //TODO VERIFICAR QUE EL ARDUINO ESTE CONECTADO
            //TODO INTENTAR LEER TAG
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Tratar de leer");
        alert.show();
    }

    public void launchLoginView(ActionEvent actionEvent) throws IOException {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        estadoArduinoCircle.setFill(Color.GREY);
        infoTagCircle.setFill(Color.GREY);
        estadoArduinoLabel.setText("Estado:DESCONECTADO");
        puertoArduinoLabel.setText("Puerto:"+InfoTagReader.getPuerto());
        infoTagLabel.setText("No Information");

        puertosComboBox.setItems(obtenerListaPuertos());
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
            puertoArduinoLabel.setText("Puerto:"+InfoTagReader.getPuerto());
        }
    }
}
