package Controladores;

import Api.ArgosInterface;
import Modelos.*;
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
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministrarEnfermedadesController implements Initializable {

    @FXML
    private AnchorPane administrarEnfermedadesAnchorPane;
    @FXML
    private ListView enfermedadesListView;
    @FXML
    private ProgressIndicator administrarEnfermedadesProgressIndicator;
    @FXML
    private RadioButton enfermedadAnteriorRadioButton;
    @FXML
    private RadioButton enfermedadCronicaRadioButton;
    @FXML
    private TextArea descripcionTextArea;
    @FXML
    private TextArea observacionesTextArea ;
    @FXML
    private ToggleButton agregarEnfermedadButton;
    @FXML
    private Button guardarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button eliminarEnfermedadesButton;
    @FXML
    private Button volverAdministrarAnimalesButton;
    final ToggleGroup tipoEnfermedadToggleGroup=new ToggleGroup();
    private String idAnimal=null;
    private String idEnfermedad=null;
    private ArgosInterface mApiInterface;
    private List<Enfermedad> listaEnfermedades=null;
    private ObservableList<String> enfermedadesObservableList=FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Obtener id animal
        idAnimal=AdministrarAnimalesController.getInstance().getIdAnimal();
        //Crear instancia cliente
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        //Deshabilitar inputs,botones y listview
        disableInputs();
        eliminarButton.setDisable(true);
        guardarButton.setDisable(true);
        enfermedadesListView.setDisable(true);
        //Cargar listview
        getListaEnfermedades();
        //Comportamiento de los radio buttons
        enfermedadAnteriorRadioButton.setToggleGroup(tipoEnfermedadToggleGroup);
        enfermedadCronicaRadioButton.setToggleGroup(tipoEnfermedadToggleGroup);
       //Comportamiento boton agregar enfermedad
        agregarEnfermedadButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    agregarEnfermedadButton.textProperty().setValue("Cancelar");
                    resetInputs();
                    enableInputs();
                    guardarButton.setDisable(false);
                    enfermedadesListView.getSelectionModel().clearSelection();
                    eliminarButton.setDisable(true);
                    eliminarEnfermedadesButton.setDisable(true);
                }else {
                    agregarEnfermedadButton.textProperty().setValue("Agregar Enfermedad");
                    disableInputs();
                    resetInputs();
                    guardarButton.setDisable(true);
                    enfermedadesListView.getSelectionModel().clearSelection();
                    eliminarButton.setDisable(true);
                    eliminarEnfermedadesButton.setDisable(false);

                }

            }
        });

    }

    //Metodo para obtener enfermedades
    private void getListaEnfermedades(){
        listaEnfermedades=null;
        enfermedadesListView.getItems().clear();
        administrarEnfermedadesProgressIndicator.setVisible(true);
        Call<Enfermedades> getEnfermedadesCall=mApiInterface.getEnfermedades(SesionInfo.getClaveUsuario(),idAnimal);
        getEnfermedadesCall.enqueue(new Callback<Enfermedades>() {
            @Override
            public void onResponse(Call<Enfermedades> call, Response<Enfermedades> response) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarEnfermedadesProgressIndicator.setVisible(false);
                            if (response.isSuccessful()){
                                listaEnfermedades=response.body().getEnfermedades();
                                for (int i=0;i<listaEnfermedades.size();i++){
                                    enfermedadesObservableList.add(i,listaEnfermedades.get(i).getDescripcion());
                                }
                                enfermedadesListView.setItems(enfermedadesObservableList);
                                enfermedadesListView.setDisable(false);

                            }else{
                                enfermedadesListView.setDisable(true);
                            }

                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();
            }

            @Override
            public void onFailure(Call<Enfermedades> call, Throwable throwable) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarEnfermedadesProgressIndicator.setVisible(false);
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
        enfermedadCronicaRadioButton.selectedProperty().setValue(false);
        enfermedadAnteriorRadioButton.selectedProperty().setValue(false);
        descripcionTextArea.setText(null);
        observacionesTextArea.setText(null);
    }

    //Habilitar inputs
    private void enableInputs(){
        enfermedadCronicaRadioButton.setDisable(false);
        enfermedadAnteriorRadioButton.setDisable(false);
        descripcionTextArea.setDisable(false);
        observacionesTextArea.setDisable(false);
    }
    //Deshabilitar inputs
    private void disableInputs(){
        enfermedadCronicaRadioButton.setDisable(true);
        enfermedadAnteriorRadioButton.setDisable(true);
        descripcionTextArea.setDisable(true);
        observacionesTextArea.setDisable(true);
    }

    //Listener para el list view de enfermedades
    public void enfermedadSeleccionada(MouseEvent mouseEvent) {
        Integer i=enfermedadesListView.getSelectionModel().getSelectedIndex();
        if (i>=0){
            idEnfermedad=i.toString();
            displayEnfermedadSeleccionada(i);
            eliminarButton.setDisable(false);
        }else{
            eliminarButton.setDisable(true);
        }
    }

    //Mostrar en el display la informacion de la enfermedad seleccionada
    private void displayEnfermedadSeleccionada(Integer i)  {
        Enfermedad enfermedad=listaEnfermedades.get(i);
        descripcionTextArea.setText(enfermedad.getDescripcion());
        if (enfermedad.getEnfermedadAnterior()!=null){
            if (Integer.valueOf(enfermedad.getEnfermedadAnterior())==1){
                enfermedadAnteriorRadioButton.selectedProperty().setValue(true);
            }
        }else {
            enfermedadAnteriorRadioButton.selectedProperty().setValue(false);
        }
        if (enfermedad.getCronica()!=null) {
            if (Integer.valueOf(enfermedad.getCronica())==1){
                enfermedadCronicaRadioButton.selectedProperty().setValue(true);
            }
        }else {
            enfermedadCronicaRadioButton.selectedProperty().setValue(false);
        }

        if (enfermedad.getObservaciones()!=null){
            observacionesTextArea.setText(enfermedad.getObservaciones());
        }else {
            observacionesTextArea.setText(null);
        }
    }

    //Guardar nueva enfermedad
    public void guardarEnfermedad(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (descripcionTextArea.getText()!=null){
            if (ValidadorInputs.validarDescripcionInput(descripcionTextArea.getText())){
                String descripcion=descripcionTextArea.getText();
                String observaciones=null;
                String enfermedadAnterior=null;
                String enfermedadCronica=null;
                if (observacionesTextArea.getText()!=null){
                    if (ValidadorInputs.validarObservaciones(observacionesTextArea.getText())){
                        observaciones=observacionesTextArea.getText();
                    }
                }
                if (enfermedadAnteriorRadioButton.selectedProperty().getValue()!=null){
                    if (enfermedadAnteriorRadioButton.selectedProperty().getValue()){
                        enfermedadAnterior="1";
                        enfermedadCronica="0";
                    }
                }
                if (enfermedadCronicaRadioButton.selectedProperty().getValue()!=null){
                    if (enfermedadCronicaRadioButton.selectedProperty().getValue()){
                        enfermedadAnterior="0";
                        enfermedadCronica="1";
                    }
                }
                Enfermedad nuevaEnfermedad=new Enfermedad(null,enfermedadCronica,enfermedadAnterior,descripcion,observaciones,null,null);
                Call<ApiRespuesta> agregarEnfermedadCall=mApiInterface.agregarEnfermedad(SesionInfo.getClaveUsuario(),idAnimal,nuevaEnfermedad);
                agregarEnfermedadCall.enqueue(new Callback<ApiRespuesta>() {
                    @Override
                    public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                        Runnable runnable=()->{
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()){
                                        alert.setContentText(response.body().getMensaje());
                                        getListaEnfermedades();
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
                agregarEnfermedadButton.setSelected(false);
                guardarButton.setDisable(true);
                enfermedadesListView.getSelectionModel().clearSelection();
                eliminarButton.setDisable(true);
                eliminarEnfermedadesButton.setDisable(false);
            }else{
                alert.setContentText("Descripcion invalida");
                alert.show();
            }

        }else{
            alert.setContentText("Debe indicar una descripcion para la enfermedad");
            alert.show();
        }


    }

    //Eliminar enfermedad seleccionada
    public void eliminarEnfermedad(ActionEvent actionEvent) {
        administrarEnfermedadesAnchorPane.setDisable(true);
        String idEnfermedadSeleccionada=listaEnfermedades.get(Integer.valueOf(idEnfermedad)).getIdEnfermedad();
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                listaEnfermedades.get(Integer.valueOf(idEnfermedad)).getDescripcion(),
                ButtonType.OK,ButtonType.CANCEL);
        alert.setTitle("Se eliminara la enfermedad:");
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarEnfermedadCall=mApiInterface.eliminarEnfermedad(SesionInfo.getClaveUsuario(),idAnimal,idEnfermedadSeleccionada);
            eliminarEnfermedadCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    respuestaDialog.setContentText(response.body().getMensaje());
                                    getListaEnfermedades();
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
        enfermedadesListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarEnfermedadesAnchorPane.setDisable(false);

    }

    //Eliminar TODAS las enfermedades registradas
    public void eliminarTodasLasEnfermedades(ActionEvent actionEvent) {
        administrarEnfermedadesAnchorPane.setDisable(true);
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                "Esta seguro de eliminar TODAS las enfermedades",
                ButtonType.OK,ButtonType.CANCEL);
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarEnfermedadesCall=mApiInterface.eliminarTodasLasEnfermedades(SesionInfo.getClaveUsuario(),idAnimal);
            eliminarEnfermedadesCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    respuestaDialog.setContentText(response.body().getMensaje());
                                    getListaEnfermedades();
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
        enfermedadesListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarEnfermedadesAnchorPane.setDisable(false);

    }

    //Volver al administrador de animales
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

       /*
     Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();
     */
}
