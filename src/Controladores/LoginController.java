package Controladores;

import Api.ArgosInterface;
import Modelos.ApiRespuesta;
import Modelos.LoginBody;
import Modelos.Usuario;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.CheckConexion;
import Utilidades.ValidadorInputs;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.security.jca.GetInstance;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField unmaskedPasswordTextField;
    @FXML
    private CheckBox mostrarPasswordCheckBox;
    @FXML
    private Button loginButton;
    @FXML
    private Button registrarmeButton;
    @FXML
    private Button leerTagButton;
    @FXML
    private ProgressIndicator loginProgressIndicator;
    private ArgosInterface mApiInterface;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //--Comportamiento de los text field para mostrar o ocultar el password--//
        unmaskedPasswordTextField.setVisible(false);
        unmaskedPasswordTextField.setManaged(false);

        unmaskedPasswordTextField.managedProperty().bind(mostrarPasswordCheckBox.selectedProperty());
        unmaskedPasswordTextField.visibleProperty().bind(mostrarPasswordCheckBox.selectedProperty());

        passwordTextField.managedProperty().bind(mostrarPasswordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(mostrarPasswordCheckBox.selectedProperty().not());

        unmaskedPasswordTextField.textProperty().bindBidirectional(passwordTextField.textProperty());
        //---------------------------------------------------------------------- //

        //Se desabilitaran las opciones de login y registro si no se puede conectar al servidor
        disableComponents();
        leerTagButton.setDisable(false);
        loginProgressIndicator.setVisible(true);
        Runnable runnable=()->{
            if (CheckConexion.conectarServer()){
                enableComponents();
                registrarmeButton.setDisable(false);
            }
            loginProgressIndicator.setVisible(false);

        };
        Thread thread=new Thread(runnable);
        thread.start();
    }


    //Metodo para loguear usuario
    public void launchGestorTagView(ActionEvent actionEvent) {
        loginProgressIndicator.setVisible(true);
        disableComponents();
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        LoginBody loginBody=new LoginBody(usernameTextField.getText(),passwordTextField.getText());
        //Verificar inputs
        if (ValidadorInputs.validarLoginInputs(loginBody)){
            mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
            Call<Usuario> loginCall=mApiInterface.loguearUsuario(loginBody);
            loginCall.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    Runnable runnableOnResponse=()->{
                      Platform.runLater(new Runnable() {
                          @Override
                          public void run() {
                              if (response.isSuccessful()){
                                  SesionInfo.loguearUsuario(response.body());
                                  //Launch menu gestor tags
                                  loginButton.getScene().getWindow().hide();
                                  Parent parentGestorTags = null;
                                  try {
                                      parentGestorTags = FXMLLoader.load(getClass().getResource("/Vistas/GestorTagsView.fxml"));
                                      Scene GestorTagsScene=new Scene(parentGestorTags);
                                      Stage GestorTagsStage=new Stage();
                                      GestorTagsStage.setResizable(false);
                                      GestorTagsStage.getIcons().add(new Image("/Imgs/IconV32.png"));
                                      GestorTagsStage.setScene(GestorTagsScene);
                                      GestorTagsStage.setTitle("Argos");
                                      GestorTagsStage.show();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                              }else{
                                  ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                  alert.setContentText(error.getMensaje());
                                  alert.show();
                              }
                              enableComponents();
                              clearInputs();
                              loginProgressIndicator.setVisible(false);
                          }
                      });
                    };
                    Thread thread=new Thread(runnableOnResponse);
                    thread.start();
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable throwable) {
                    Runnable runnableOnFailure=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                alert.setContentText(throwable.getLocalizedMessage());
                                alert.show();
                                enableComponents();
                                clearInputs();
                                loginProgressIndicator.setVisible(false);
                            }
                        });
                    };
                    Thread thread=new Thread(runnableOnFailure);
                    thread.start();
                }
            });
        }else{
            loginProgressIndicator.setVisible(false);
            enableComponents();
            clearInputs();
            alert.setContentText("Inputs invalidos");
            alert.show();
        }

    }
    //Mostrar vista para registrar
    public void launchRegistrarmeView(ActionEvent actionEvent) throws IOException {
        registrarmeButton.getScene().getWindow().hide();
        Parent parentRegistro=FXMLLoader.load(getClass().getResource("/Vistas/RegistroView.fxml"));
        Scene RegistroScene=new Scene(parentRegistro);
        Stage RegistroStage=new Stage();
        RegistroStage.setResizable(false);
        RegistroStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        RegistroStage.setScene(RegistroScene);
        RegistroStage.setTitle("Argos - Registro");
        RegistroStage.show();

    }

    //Mostrar vista para leer anonimamente un tag
    public void launchAnonLeerTagView(ActionEvent actionEvent) throws IOException {
        leerTagButton.getScene().getWindow().hide();
        Parent parentAnonLeerTag=FXMLLoader.load(getClass().getResource("/Vistas/AnonLeerTagView.fxml"));
        Scene AnonLeerTagScene=new Scene(parentAnonLeerTag);
        Stage AnonLeerTagStage=new Stage();
        AnonLeerTagStage.setResizable(false);
        AnonLeerTagStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AnonLeerTagStage.setScene(AnonLeerTagScene);
        AnonLeerTagStage.setTitle("Argos - Leer tag");
        AnonLeerTagStage.show();
    }



    //Metodo para borrar inputs
    private void clearInputs(){
        usernameTextField.setText("");
        passwordTextField.setText("");
    }
    //Metodos para habilitar o desabilitar componentes
    private void disableComponents(){
        usernameTextField.setDisable(true);
        passwordTextField.setDisable(true);
        unmaskedPasswordTextField.setDisable(true);
        mostrarPasswordCheckBox.setDisable(true);
        leerTagButton.setDisable(true);
        loginButton.setDisable(true);
        registrarmeButton.setDisable(true);
    }
    private void enableComponents(){
        usernameTextField.setDisable(false);
        passwordTextField.setDisable(false);
        mostrarPasswordCheckBox.setDisable(false);
        unmaskedPasswordTextField.setDisable(false);
        leerTagButton.setDisable(false);
        loginButton.setDisable(false);
        registrarmeButton.setDisable(false);
    }

    //Metodo prueba que devuelva un user prueba
    private boolean validarUser(String username,String password){
        String testUsername="skmedina";
        String testPassword="password";

        if (testUsername.equals(username)&&testPassword.equals(password)){
            return true;
        }else{
            return false;
        }
    }

    public void cambiarDomainServer(MouseEvent mouseEvent) {
        ApiClient.setDomain("192.162.1.17");
        Alert alert=new Alert(Alert.AlertType.INFORMATION,ApiClient.BASE_URL,ButtonType.OK);
        alert.show();

    }
}
