package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AcercaDeArgosController {

    @FXML
    private Button verManualButton;
    @FXML
    private Button volverAlGestorTagsButton;

    //Abrir manual de usuario
    public void abrirManualDeUsuario(ActionEvent actionEvent) {
    }

    //Volver al gestor Tags
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
}
