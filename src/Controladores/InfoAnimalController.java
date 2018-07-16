package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoAnimalController implements Initializable {
    @FXML
    private Label idLabel;
    @FXML
    private Label nombreLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Label celularLabel;
    @FXML
    private Label cordenadasLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label nombreAnimalLabel;
    @FXML
    private Label especieLabel;
    @FXML
    private Label razaLabel;
    @FXML
    private ComboBox vacunasComboBox;
    @FXML
    private ComboBox enfermedadesComboBox;
    @FXML
    private ComboBox cirujiasComboBox;
    @FXML
    private Label animalCallejeroLabel;
    @FXML
    private Label edadLabel;
    @FXML
    private Label sexoLabel;
    @FXML
    private Label pesoLabel;
    @FXML
    private Label desparasitadoLabel;
    @FXML
    private Button volverAlGestorTagsButton;


    //Volver al gestor tags
    public void launchGestorTagsView(ActionEvent actionEvent) throws IOException {
        volverAlGestorTagsButton.getScene().getWindow().hide();
        Parent parentGestorTags= null;
        parentGestorTags = FXMLLoader.load(getClass().getResource("/Vistas/GestorTagsView.fxml"));
        Scene GestorTagsScene=new Scene(parentGestorTags);
        Stage GestorTagsStage=new Stage();
        GestorTagsStage.setResizable(false);
        GestorTagsStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        GestorTagsStage.setScene(GestorTagsScene);
        GestorTagsStage.setTitle("Argos");
        GestorTagsStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
