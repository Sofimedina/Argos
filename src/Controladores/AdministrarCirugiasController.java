package Controladores;

import Api.ArgosInterface;
import Modelos.ApiRespuesta;
import Modelos.Cirugia;
import Modelos.Cirugias;
import Modelos.Vacuna;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.ValidadorInputs;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministrarCirugiasController implements Initializable {

    @FXML
    private AnchorPane administrarCirugiasAnchorPane;
    @FXML
    private ListView cirugiasListView;
    @FXML
    private ProgressIndicator administrarCirugiasProgressIndicator;
    @FXML
    private ToggleButton agregarCirugiaButton;
    @FXML
    private Button guardarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button eliminarCirugiasButton;
    @FXML
    private TextArea descripcionTextArea;
    @FXML
    private DatePicker fechaCirugiaDatePicker;
    @FXML
    private TextField veterinariaTextField;
    @FXML
    private Button volverAdministrarAnimalesButton;
    private String idAnimal=null;
    private String idCirugia=null;
    private ArgosInterface mApiInterface;
    private List<Cirugia> listaCirugias=null;
    private ObservableList<String> cirugiasObservableList=FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Obtener id animal
        idAnimal=AdministrarAnimalesController.getInstance().getIdAnimal();

        //Crear instancia cliente
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        disableInputs();
        eliminarButton.setDisable(true);
        guardarButton.setDisable(true);
        cirugiasListView.setDisable(true);
        getListaCirugias();

        agregarCirugiaButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                 if (newValue){
                     agregarCirugiaButton.textProperty().setValue("Cancelar");
                     resetInputs();
                     enableInputs();
                     guardarButton.setDisable(false);
                     cirugiasListView.getSelectionModel().clearSelection();
                     eliminarButton.setDisable(true);
                     eliminarCirugiasButton.setDisable(true);
                }else {
                     agregarCirugiaButton.textProperty().setValue("Agregar Cirugia");
                     disableInputs();
                     resetInputs();
                     guardarButton.setDisable(true);
                     cirugiasListView.getSelectionModel().clearSelection();
                     eliminarButton.setDisable(true);
                     eliminarCirugiasButton.setDisable(false);

                }

            }
        });

    }



    //Metodo para obtener cirugias
    private void getListaCirugias(){
        listaCirugias=null;
        cirugiasListView.getItems().clear();
        administrarCirugiasProgressIndicator.setVisible(true);
        Call<Cirugias> getCirugiasCall=mApiInterface.getCirugias(SesionInfo.getClaveUsuario(),idAnimal);
        getCirugiasCall.enqueue(new Callback<Cirugias>() {
            @Override
            public void onResponse(Call<Cirugias> call, Response<Cirugias> response) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarCirugiasProgressIndicator.setVisible(false);
                            if (response.isSuccessful()){
                                listaCirugias=response.body().getCirugias();
                                for (int i=0;i<listaCirugias.size();i++){
                                    cirugiasObservableList.add(i,listaCirugias.get(i).getDescripcion());
                                }
                                cirugiasListView.setItems(cirugiasObservableList);
                                cirugiasListView.setDisable(false);

                            }else{
                                cirugiasListView.setDisable(true);
                            }

                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();
            }

            @Override
            public void onFailure(Call<Cirugias> call, Throwable throwable) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarCirugiasProgressIndicator.setVisible(false);
                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();

            }
        });

    }

    //Metodo para resetear inputs
    private void resetInputs(){
        descripcionTextArea.setText(null);
        fechaCirugiaDatePicker.setValue(null);
        veterinariaTextField.setText(null);
    }


    //Listener para el listview de cirugias
    public void cirugiaSeleccionada(MouseEvent mouseEvent) throws ParseException {
        Integer i=cirugiasListView.getSelectionModel().getSelectedIndex();
        if (i>=0){
            idCirugia=i.toString();
            displayCirugiaSeleccionada(i);
            eliminarButton.setDisable(false);
        }else{
            eliminarButton.setDisable(true);
        }

    }

    //Mostrar en el display la informacion de la cirugia seleccionada
    private void displayCirugiaSeleccionada(Integer i) throws ParseException {
        Cirugia cirugia=listaCirugias.get(i);
        descripcionTextArea.setText(cirugia.getDescripcion());
        fechaCirugiaDatePicker.setValue(createDateFromString(cirugia.getFecha()));
        if (cirugia.getVeterinaria()!=null){
            veterinariaTextField.setText(cirugia.getVeterinaria());
        }else{
            veterinariaTextField.setText(null);
        }

    }

    //Crear date from String
    private LocalDate createDateFromString(String fechaString) throws ParseException {
        DateFormat format=new SimpleDateFormat("yyyy-mm-dd");
        Date fecha=format.parse(fechaString);
        LocalDate fechaFormateada=fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fechaFormateada;
    }

    //Guardar cirugia en el server
    public void guardarCirugia(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (descripcionTextArea.getText()!=null){
            if (ValidadorInputs.validarDescripcionInput(descripcionTextArea.getText())){
                if (fechaCirugiaDatePicker.getValue()!=null){
                    String descripcion=descripcionTextArea.getText();
                    String fecha=fechaCirugiaDatePicker.getValue().toString();
                    String veterinaria=null;
                    if (veterinariaTextField.getText()!=null){
                        if (ValidadorInputs.validarVeterinaria(veterinariaTextField.getText())){
                            veterinaria=veterinariaTextField.getText();
                        }
                    }
                    Cirugia nuevaCirugia=new Cirugia(null,descripcion,fecha,veterinaria,null,null);
                    Call<ApiRespuesta> agregarCirugiaCall=mApiInterface.agregarCirugia(SesionInfo.getClaveUsuario(),idAnimal,nuevaCirugia);
                    agregarCirugiaCall.enqueue(new Callback<ApiRespuesta>() {
                        @Override
                        public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                            Runnable runnable=()->{
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.isSuccessful()){
                                            alert.setContentText(response.body().getMensaje());
                                            getListaCirugias();
                                        }else{
                                            ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                            alert.setContentText(error.getMensaje());
                                        }
                                        alert.show();
                                    }
                                });
                            };
                            Thread thread=new Thread(runnable);
                            thread.start();

                        }

                        @Override
                        public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                            Runnable runnable=()->{
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        alert.setContentText(throwable.getLocalizedMessage());
                                        alert.show();
                                    }
                                });
                            };
                            Thread thread=new Thread(runnable);
                            thread.start();
                        }
                    });
                    resetInputs();
                    agregarCirugiaButton.setSelected(false);
                    guardarButton.setDisable(true);
                    eliminarCirugiasButton.setDisable(false);
                    cirugiasListView.getSelectionModel().clearSelection();
                    eliminarButton.setDisable(true);
                }else{
                    alert.setContentText("Debe indicar la fecha de aplicacion");
                    alert.show();
                }
            }else{
                alert.setContentText("Descripcion invalida");
                alert.show();
            }
        }else{
            alert.setContentText("Debe indicar una descripcion para la cirugia");
            alert.show();
        }

    }

    //Eliminar cirugia seleccionada
    public void eliminarCirugia(ActionEvent actionEvent) {
        administrarCirugiasAnchorPane.setDisable(true);
        String idCirugiaSeleccionada=listaCirugias.get(Integer.valueOf(idCirugia)).getIdCirugia();
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                listaCirugias.get(Integer.valueOf(idCirugia)).getDescripcion(),
                ButtonType.OK,ButtonType.CANCEL);
        alert.setTitle("Se eliminara la cirugia:");
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarCirugiaCall=mApiInterface.eliminarCirugia(SesionInfo.getClaveUsuario(),idAnimal,idCirugiaSeleccionada);
            eliminarCirugiaCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    respuestaDialog.setContentText(response.body().getMensaje());
                                    getListaCirugias();
                                }else{
                                    ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                    respuestaDialog.setContentText(error.getMensaje());
                                }
                                respuestaDialog.show();
                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();
                }

                @Override
                public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                respuestaDialog.setContentText(throwable.getLocalizedMessage());
                                respuestaDialog.show();

                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();
                }
            });

        }
        cirugiasListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarCirugiasAnchorPane.setDisable(false);

    }


    //Eliminar todas las cirugias registradas
    public void elliminarTodasLasCirugias(ActionEvent actionEvent) {
        administrarCirugiasAnchorPane.setDisable(true);
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                "Esta seguro de eliminar TODAS las cirugias",
                ButtonType.OK,ButtonType.CANCEL);
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarCirugiasCall=mApiInterface.eliminarTodasLasCirugias(SesionInfo.getClaveUsuario(),idAnimal);
            eliminarCirugiasCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    respuestaDialog.setContentText(response.body().getMensaje());
                                    getListaCirugias();
                                }else{
                                    ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                    respuestaDialog.setContentText(error.getMensaje());
                                }
                                respuestaDialog.show();

                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();
                }

                @Override
                public void onFailure(Call<ApiRespuesta> call, Throwable throwable) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                respuestaDialog.setContentText(throwable.getLocalizedMessage());
                                respuestaDialog.show();
                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();

                }
            });

        }
        cirugiasListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarCirugiasAnchorPane.setDisable(false);

    }

    //Habilitar inputs
    private void enableInputs(){
        descripcionTextArea.setDisable(false);
        fechaCirugiaDatePicker.setDisable(false);
        veterinariaTextField.setDisable(false);
    }
    //Deshabilitar inputs
    private void disableInputs(){
        descripcionTextArea.setDisable(true);
        fechaCirugiaDatePicker.setDisable(true);
        veterinariaTextField.setDisable(true);
    }

    //Metodo para volver al administrador de animales
    public void launchAdministrarAnimalesView(ActionEvent actionEvent) throws IOException {
        volverAdministrarAnimalesButton.getScene().getWindow().hide();
        Parent parentAdministrarAnimales= null;
        parentAdministrarAnimales = FXMLLoader.load(getClass().getResource("/Vistas/AdministrarAnimalesView.fxml"));
        Scene AdministrarAnimalesScene=new Scene(parentAdministrarAnimales);
        Stage AdministrarAnimalesStage=new Stage();
        AdministrarAnimalesStage.setResizable(false);
        AdministrarAnimalesStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AdministrarAnimalesStage.setScene(AdministrarAnimalesScene);
        AdministrarAnimalesStage.setTitle("Argos - Administrar animales");
        AdministrarAnimalesStage.show();
    }



}
