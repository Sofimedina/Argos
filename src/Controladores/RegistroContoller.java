package Controladores;

import Api.ArgosInterface;
import Modelos.ApiRespuesta;
import Modelos.Usuario;
import Retrofit.ApiClient;
import Utilidades.CheckConexion;
import Utilidades.ValidadorInputs;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class RegistroContoller  implements Initializable {
    @FXML
    ProgressIndicator registroProgressIndicator;
    @FXML
    private Button volverAlLoginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField unmaskedPasswordTextField;
    @FXML
    private CheckBox mostrarPasswordCheckBox;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField apellidoTextField;
    @FXML
    private TextField telefonoFijoTextField;
    @FXML
    private TextField celularTextField;
    @FXML
    private TextField direccionTextField;
    @FXML
    private Button registrarmeButton;

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

        //Se desabilitaran la opcion de registro si no se puede conectar al servidor
        disableComponents();
        registrarmeButton.setDisable(true);
        registroProgressIndicator.setVisible(true);
        Runnable runnable=()->{
            //TimeUnit.SECONDS.sleep(1);
            if (CheckConexion.conectarServer()){
                enableComponents();
                registrarmeButton.setDisable(false);
            }
            registroProgressIndicator.setVisible(false);

        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

    //Mostrar vista login
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

    //Intentar registrar usuario
    public void registrarUsuario(ActionEvent actionEvent) {
        Usuario usuarioAregistrar=new Usuario(null,null,usernameTextField.getText(),passwordTextField.getText(),
                emailTextField.getText(),null,nombreTextField.getText(),apellidoTextField.getText(),
                telefonoFijoTextField.getText(),celularTextField.getText(),null,null,
                direccionTextField.getText(),null);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        disableComponents();
        //Validar inputs
        if (ValidadorInputs.validarRegistroInputs(usuarioAregistrar)){
            mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
            Call<ApiRespuesta> registroCall=mApiInterface.registrarUsuario(usuarioAregistrar);
            registroCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnableOnResponse=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    alert.setContentText(response.body().getMensaje());
                                    alert.show();
                                }else{
                                    ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                    alert.setContentText(error.getMensaje());
                                    alert.show();
                                }
                                clearInputs();
                                enableComponents();
                            }

                        });
                    };
                    Thread thread = new Thread(runnableOnResponse);
                    thread.start();
                }

                @Override
                public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                    Runnable runnableOnFailure=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                alert.setContentText(throwable.getLocalizedMessage());
                                alert.show();
                                enableComponents();
                                clearInputs();
                            }
                        });
                    };
                    Thread thread=new Thread(runnableOnFailure);
                    thread.start();
                }
            });
        }else{
            alert.setContentText("Datos ingresados invalidos");
            enableComponents();
            clearInputs();
            alert.show();
        }
    }


    //Metodo para borrar inputs
    private void clearInputs(){
        usernameTextField.setText("");
        passwordTextField.setText("");
        emailTextField.setText("");
        nombreTextField.setText("");
        apellidoTextField.setText("");
        telefonoFijoTextField.setText("");
        celularTextField.setText("");
        direccionTextField.setText("");
    }
    //Metodos para habilitar o desabilitar componentes
    private void disableComponents(){
        usernameTextField.setDisable(true);
        passwordTextField.setDisable(true);
        unmaskedPasswordTextField.setDisable(true);
        mostrarPasswordCheckBox.setDisable(true);
        emailTextField.setDisable(true);
        nombreTextField.setDisable(true);
        apellidoTextField.setDisable(true);
        telefonoFijoTextField.setDisable(true);
        celularTextField.setDisable(true);
        direccionTextField.setDisable(true);
        registrarmeButton.setDisable(true);
    }
    private void enableComponents(){
        usernameTextField.setDisable(false);
        passwordTextField.setDisable(false);
        mostrarPasswordCheckBox.setDisable(false);
        unmaskedPasswordTextField.setDisable(false);
        emailTextField.setDisable(false);
        nombreTextField.setDisable(false);
        apellidoTextField.setDisable(false);
        telefonoFijoTextField.setDisable(false);
        celularTextField.setDisable(false);
        direccionTextField.setDisable(false);
        registrarmeButton.setDisable(false);
    }

    //Intentar registro del usuario con el server
    private void registrarUsuarioEnServer(Usuario usuarioAregistrar){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        Call<ApiRespuesta> registroCall=mApiInterface.registrarUsuario(usuarioAregistrar);
        registroCall.enqueue(new Callback<ApiRespuesta>() {
            @Override
            public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                if (response.isSuccessful()){
                    alert.setContentText(response.message());
                    alert.show();
                }else {
                    ApiRespuesta apiRespuesta=ApiRespuesta.fromResponseBody(response.errorBody());
                    alert.setContentText(apiRespuesta.getMensaje());
                    alert.show();
                }
            }
            @Override
            public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                alert.setContentText(throwable.getLocalizedMessage());
                alert.show();
            }
        });

    }
}

