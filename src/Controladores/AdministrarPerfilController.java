package Controladores;

import Api.ArgosInterface;
import Modelos.ApiRespuesta;
import Modelos.Usuario;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.CheckConexion;
import Utilidades.ValidadorInputs;
import com.sun.deploy.security.SessionCertStore;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministrarPerfilController implements Initializable {
    @FXML
    private javafx.scene.control.TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private ToggleButton nuevoPasswordButton;
    @FXML
    private Button guardarPasswordButton;
    @FXML
    private javafx.scene.control.TextField unmaskedPasswordTextField;
    @FXML
    private CheckBox mostrarPasswordCheckBox;
    @FXML
    private javafx.scene.control.TextField emailTextField;
    @FXML
    private javafx.scene.control.TextField nombreTextField;
    @FXML
    private javafx.scene.control.TextField apellidoTextFIeld;
    @FXML
    private javafx.scene.control.TextField telefonoTextFIeld;
    @FXML
    private TextField celularTextFIeld;
    @FXML
    private javafx.scene.control.TextArea direccionTextArea;
    @FXML
    private javafx.scene.control.Button volverAlGestorTagButton;
    @FXML
    private ToggleButton editarButton;
    @FXML
    private javafx.scene.control.Button guardarButton;
    @FXML
    private javafx.scene.control.Button borrarCuentaButton;
    @FXML
    private ProgressIndicator registroProgressIndicator;
    @FXML
    private AnchorPane administrarPerfilAnchorPane;
    private Usuario usuarioLogueado=null;
    private ArgosInterface mApiInterface;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        //--Comportamiento de los text field para mostrar o ocultar el password--//
        unmaskedPasswordTextField.setVisible(false);
        unmaskedPasswordTextField.setManaged(false);

        unmaskedPasswordTextField.managedProperty().bind(mostrarPasswordCheckBox.selectedProperty());
        unmaskedPasswordTextField.visibleProperty().bind(mostrarPasswordCheckBox.selectedProperty());

        passwordTextField.managedProperty().bind(mostrarPasswordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(mostrarPasswordCheckBox.selectedProperty().not());

        unmaskedPasswordTextField.textProperty().bindBidirectional(passwordTextField.textProperty());
        //---------------------------------------------------------------------- //
        disableInputs();
        editarButton.setDisable(true);
        borrarCuentaButton.setDisable(true);
        guardarButton.setDisable(true);
        nuevoPasswordButton.setDisable(true);
        passwordTextField.setDisable(true);
        unmaskedPasswordTextField.setDisable(true);
        mostrarPasswordCheckBox.setDisable(true);
        guardarPasswordButton.setDisable(true);
        registroProgressIndicator.setVisible(true);
        getInfoUser();
    }

    //Volver al gestor de tags
    public void launchGestorTagsView(ActionEvent actionEvent) throws IOException {
        volverAlGestorTagButton.getScene().getWindow().hide();
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

    //Metodo para desabilitar inputs
    private void disableInputs(){
        usernameTextField.setDisable(true);
        emailTextField.setDisable(true);
        nombreTextField.setDisable(true);
        apellidoTextFIeld.setDisable(true);
        telefonoTextFIeld.setDisable(true);
        celularTextFIeld.setDisable(true);
        direccionTextArea.setDisable(true);
    }
    //Metodo para habilitar inputs
    private void enableInputs(){
        usernameTextField.setDisable(false);
        emailTextField.setDisable(false);
        nombreTextField.setDisable(false);
        apellidoTextFIeld.setDisable(false);
        telefonoTextFIeld.setDisable(false);
        celularTextFIeld.setDisable(false);
        direccionTextArea.setDisable(false);
    }

    public void nuevoPassword(ActionEvent actionEvent) {
        if (nuevoPasswordButton.isSelected()){
            nuevoPasswordButton.textProperty().setValue("Cancelar");
            passwordTextField.setText(null);
            passwordTextField.setDisable(false);
            unmaskedPasswordTextField.setDisable(false);
            mostrarPasswordCheckBox.setDisable(false);
            guardarPasswordButton.setDisable(false);
            borrarCuentaButton.setDisable(true);
            editarButton.setDisable(true);
            guardarButton.setDisable(true);
        }else {
            nuevoPasswordButton.textProperty().setValue("Nuevo Password");
            passwordTextField.setText("xxxxxx");
            passwordTextField.setDisable(true);
            unmaskedPasswordTextField.setDisable(true);
            mostrarPasswordCheckBox.setDisable(true);
            guardarPasswordButton.setDisable(true);
            borrarCuentaButton.setDisable(false);
            editarButton.setDisable(false);
            guardarButton.setDisable(false);
        }

    }

    public void guardarPassword(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        if (unmaskedPasswordTextField.getText()==null){
            alert.setContentText("Debe introducir una contraseña");
            alert.show();
        }else {
            if (ValidadorInputs.validarPassword(unmaskedPasswordTextField.getText())){
                Usuario usuarioModificado=usuarioLogueado;
                usuarioModificado.setPassword(passwordTextField.getText());
                Call<ApiRespuesta> actualizarPasswordCall=mApiInterface.cambiarPassword(SesionInfo.getClaveUsuario(),usuarioModificado);
                actualizarPasswordCall.enqueue(new Callback<ApiRespuesta>() {
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
                                }
                            });
                        };
                        Thread thread=new Thread(runnableOnResponse);
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
                                }
                            });
                        };
                        Thread thread=new Thread(runnableOnFailure);
                        thread.start();

                    }
                });
                nuevoPasswordButton.setSelected(false);
                nuevoPasswordButton.textProperty().setValue("Nuevo Password");
                passwordTextField.setText("xxxxxx");
                passwordTextField.setDisable(true);
                unmaskedPasswordTextField.setDisable(true);
                mostrarPasswordCheckBox.setDisable(true);
                guardarPasswordButton.setDisable(true);
                borrarCuentaButton.setDisable(false);
                editarButton.setDisable(false);
                guardarButton.setDisable(false);
            }else{
                alert.setContentText("Contraseña no valida");
                alert.show();
            }
        }
    }


    //Habilitar la edicion del perfil
    public void editarUsuario(ActionEvent actionEvent) {
       if (editarButton.isSelected()){
           guardarButton.setDisable(false);
           editarButton.textProperty().setValue("Cancelar");
           enableInputs();
           borrarCuentaButton.setDisable(true);
           nuevoPasswordButton.setDisable(true);
       }else {
           guardarButton.setDisable(true);
           editarButton.textProperty().setValue("Editar");
           disableInputs();
           displayInfoUsuario(usuarioLogueado);
           borrarCuentaButton.setDisable(false);
           nuevoPasswordButton.setDisable(false);
       }

    }

    //Guardar los cambios de la edicion del perfil
    public void guardarEdicionUsuario(ActionEvent actionEvent) {
     Usuario usuarioModificado=null;
       usuarioModificado=usuarioLogueado;
       Alert alert=new Alert(Alert.AlertType.INFORMATION);
       if (!usernameTextField.getText().isEmpty()&& ValidadorInputs.validarUsername(usernameTextField.getText())){
           usuarioModificado.setUsername(usernameTextField.getText());
           if (!emailTextField.getText().isEmpty()&&ValidadorInputs.validarEmail(emailTextField.getText())){
               usuarioModificado.setEmail(emailTextField.getText());
               if (nombreTextField.getText()!=null){
                   if (ValidadorInputs.validarNombre(nombreTextField.getText())){
                       usuarioModificado.setNombre(nombreTextField.getText());
                   }
               }
               if (apellidoTextFIeld.getText()!=null){
                   if (ValidadorInputs.validarApellido(apellidoTextFIeld.getText())){
                       usuarioModificado.setApellido(apellidoTextFIeld.getText());
                   }
               }
               if (telefonoTextFIeld.getText()!=null){
                   if (ValidadorInputs.validarTelefonoFijo(telefonoTextFIeld.getText())){
                       usuarioModificado.setTelefonoFijo(telefonoTextFIeld.getText());
                   }
               }
               if (celularTextFIeld.getText()!=null){
                   if (ValidadorInputs.validarCelular(celularTextFIeld.getText())){
                       usuarioModificado.setTelefonoCelular(celularTextFIeld.getText());
                   }
               }
               if (direccionTextArea.getText()!=null){
                   if (ValidadorInputs.validarDireccion(direccionTextArea.getText())){
                       usuarioModificado.setDireccionIndicaciones(direccionTextArea.getText());
                   }
               }

               Call<ApiRespuesta> actualizarUsuarioCall=mApiInterface.actualizarUsuario(SesionInfo.getClaveUsuario(),usuarioModificado);
               actualizarUsuarioCall.enqueue(new Callback<ApiRespuesta>() {
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
                                   getInfoUser();
                               }
                           });
                       };
                       Thread thread=new Thread(runnableOnResponse);
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
                               }
                           });
                       };
                       Thread thread=new Thread(runnableOnFailure);
                       thread.start();

                   }
               });
               guardarButton.setDisable(true);
               editarButton.setSelected(false);
               editarButton.textProperty().setValue("Editar");
               disableInputs();
               borrarCuentaButton.setDisable(false);
               nuevoPasswordButton.setDisable(false);
           }else{
               alert.setContentText("Email invalido");
               alert.show();
           }
       }else{
           alert.setContentText("Username invalido");
           alert.show();
       }

    }


    //Metodo para borra cuenta
    public void borrarCuenta(ActionEvent actionEvent) throws IOException {
        Alert resultadoAlert=new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
        Alert alert=new Alert(Alert.AlertType.WARNING,"Esta seguro que desea borrar su cuenta?",ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            administrarPerfilAnchorPane.setDisable(true);
            Call<ApiRespuesta> eliminarCuentaCall=mApiInterface.eliminarCuenta(SesionInfo.getClaveUsuario());
            eliminarCuentaCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnableOnResponse=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    resultadoAlert.setContentText(response.body().getMensaje());
                                    Optional<ButtonType> dialog=resultadoAlert.showAndWait();
                                    if (dialog.get()==ButtonType.OK){
                                        borrarCuentaButton.getScene().getWindow().hide();
                                        Parent parentLogin= null;
                                        try {
                                            parentLogin = FXMLLoader.load(getClass().getResource("/Vistas/LoginView.fxml"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Scene LoginScene=new Scene(parentLogin);
                                        Stage LoginStage=new Stage();
                                        LoginStage.setResizable(false);
                                        LoginStage.getIcons().add(new Image("/Imgs/IconV32.png"));
                                        LoginStage.setScene(LoginScene);
                                        LoginStage.setTitle("Argos - Login");
                                        LoginStage.show();
                                    }

                                }else{
                                    ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                    resultadoAlert.setContentText(error.getMensaje());
                                    resultadoAlert.show();
                                    administrarPerfilAnchorPane.setDisable(false);
                                }

                            }
                        });
                    };
                    Thread thread=new Thread(runnableOnResponse);
                    thread.start();
                }

                @Override
                public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                    Runnable runnableOnFailure=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                resultadoAlert.setContentText(throwable.getLocalizedMessage());
                                resultadoAlert.show();
                                administrarPerfilAnchorPane.setDisable(false);
                            }
                        });
                    };
                    Thread thread=new Thread(runnableOnFailure);
                    thread.start();
                }
            });

        }

    }

    //Metodo para llenar los inputs con info del usuario logueado
    private void displayInfoUsuario(Usuario usuario){
        usernameTextField.setText(usuario.getUsername());
        passwordTextField.setText("xxxxxx");
        emailTextField.setText(usuario.getEmail());
        nombreTextField.setText(usuario.getNombre());
        apellidoTextFIeld.setText(usuario.getApellido());
        telefonoTextFIeld.setText(usuario.getTelefonoFijo());
        celularTextFIeld.setText(usuario.getTelefonoCelular());
        direccionTextArea.setText(usuario.getDireccionIndicaciones());
    }

    private void getInfoUser(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        Call<Usuario> leerUsuarioCall=mApiInterface.leerInfoUsuario(SesionInfo.getClaveUsuario());
        leerUsuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Runnable runnableOnResponse=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()){
                                usuarioLogueado=response.body();
                                SesionInfo.updateUsuarioLogueado(usuarioLogueado);
                                displayInfoUsuario(usuarioLogueado);
                                editarButton.setDisable(false);
                                nuevoPasswordButton.setDisable(false);
                                borrarCuentaButton.setDisable(false);
                                registroProgressIndicator.setVisible(false);

                            }else{
                                ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                alert.setContentText(error.getMensaje());
                                alert.show();
                            }
                            registroProgressIndicator.setVisible(false);
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
                            registroProgressIndicator.setVisible(false);
                        }
                    });
                };
                Thread thread=new Thread(runnableOnFailure);
                thread.start();
            }
        });
    }


}
