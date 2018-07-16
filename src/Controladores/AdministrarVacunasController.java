package Controladores;

import Api.ArgosInterface;
import Modelos.ApiRespuesta;
import Modelos.Vacuna;
import Modelos.Vacunas;
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

public class AdministrarVacunasController implements Initializable {

    @FXML
    private AnchorPane administrarVacunasAnchorPane;
    @FXML
    private ListView vacunasListView;
    @FXML
    private TextArea descripcionTextArea;
    @FXML
    private TextField veterinariaTextField;
    @FXML
    private DatePicker fechaVacunaDatePicker;
    @FXML
    private TextField nombreComercialTextField;
    @FXML
    private Button volverAdministrarAnimalesButton;
    @FXML
    private ToggleButton nuevaVacunaButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button guardarButton;
    @FXML
    private Button eliminarVacunasButton;
    @FXML
    private ProgressIndicator administrarVacunasProgressIndicator;
    private String idAnimal=null;
    private String idVacuna=null;
    private ArgosInterface mApiInterface;
    private List<Vacuna> listaVacunas=null;
    private ObservableList<String> vacunasObservableList=FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Obtener id animal
        idAnimal=AdministrarAnimalesController.getInstance().getIdAnimal();
        //Crear instancia cliente
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        disableInputs();
        inicializarComportamientoElementos();
        eliminarButton.setDisable(true);
        guardarButton.setDisable(true);
        vacunasListView.setDisable(true);
        getListaVacunas();
    }

    //Inicializar comportamiento de elementos
    private void inicializarComportamientoElementos(){
        nuevaVacunaButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                 nuevaVacunaButton.textProperty().setValue("Cancelar");
                 resetInputs();
                 enableInputs();
                 guardarButton.setDisable(false);
                 vacunasListView.getSelectionModel().clearSelection();
                 eliminarButton.setDisable(true);
                 eliminarVacunasButton.setDisable(true);
                }else {
                 nuevaVacunaButton.textProperty().setValue("Nueva Vacuna");
                 disableInputs();
                 resetInputs();
                 guardarButton.setDisable(true);
                 vacunasListView.getSelectionModel().clearSelection();
                 eliminarButton.setDisable(true);
                 eliminarVacunasButton.setDisable(false);
                }
            }
        });

    }

    //Metodo para obtener las vacunas registradas
    private void getListaVacunas(){
        listaVacunas=null;
        vacunasListView.getItems().clear();
        administrarVacunasProgressIndicator.setVisible(true);
        Call<Vacunas> getVacunasCall=mApiInterface.getVacunas(SesionInfo.getClaveUsuario(),idAnimal);
        getVacunasCall.enqueue(new Callback<Vacunas>() {
            @Override
            public void onResponse(Call<Vacunas> call, Response<Vacunas> response) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarVacunasProgressIndicator.setVisible(false);
                            if (response.isSuccessful()){
                                listaVacunas=response.body().getVacunas();
                                for (int i=0;i<listaVacunas.size();i++){
                                    vacunasObservableList.add(i,listaVacunas.get(i).getDescripcion());
                                }
                                vacunasListView.setItems(vacunasObservableList);
                                vacunasListView.setDisable(false);
                            }else{
                                vacunasListView.setDisable(true);
                            }
                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();
            }

            @Override
            public void onFailure(Call<Vacunas> call, Throwable throwable) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            administrarVacunasProgressIndicator.setVisible(false);
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
        nombreComercialTextField.setText(null);
        descripcionTextArea.setText(null);
        fechaVacunaDatePicker.setValue(null);
        veterinariaTextField.setText(null);
    }

    //Metodo para cuando se selecciona una vacuna
    public void vacunaSeleccionada(MouseEvent mouseEvent) throws ParseException {
        Integer i=vacunasListView.getSelectionModel().getSelectedIndex();
        if (i>=0){
            idVacuna=i.toString();
            displayVacunaSeleccionada(i);
            eliminarButton.setDisable(false);
        }else {
            eliminarButton.setDisable(true);
        }
    }

    //Mostrar en el display la informacion de la vacuna seleccionada
    private void displayVacunaSeleccionada(Integer i) throws ParseException {
        Vacuna vacuna=listaVacunas.get(i);
        descripcionTextArea.setText(vacuna.getDescripcion());
        fechaVacunaDatePicker.setValue(createDateFromString(vacuna.getFechaAplicacion()));
        if (vacuna.getNombreComercial()!=null){
            nombreComercialTextField.setText(vacuna.getNombreComercial());
        }else {
            nombreComercialTextField.setText(null);
        }
        if (vacuna.getVeterinaria()!=null){
            veterinariaTextField.setText(vacuna.getVeterinaria());
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

    //Metodo para borrar TODAS las vacunas registradas
    public void eliminarVacunasRegistradas(ActionEvent actionEvent) {
        administrarVacunasAnchorPane.setDisable(true);
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                "Esta seguro de eliminar TODAS las vacunas",
                ButtonType.OK,ButtonType.CANCEL);
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
           Call<ApiRespuesta> eliminarTodasLasVacunasCall=mApiInterface.eliminarTodasLasVacunas(SesionInfo.getClaveUsuario(),idAnimal);
           eliminarTodasLasVacunasCall.enqueue(new Callback<ApiRespuesta>() {
               @Override
               public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                   Runnable runnable=()->{
                       Platform.runLater(new Runnable() {
                           @Override
                           public void run() {
                               if (response.isSuccessful()){
                                   respuestaDialog.setContentText(response.body().getMensaje());
                                   getListaVacunas();
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
        vacunasListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarVacunasAnchorPane.setDisable(false);
    }

    //Eliminar vacuna seleccionada
    public void eliminarVacuna(ActionEvent actionEvent) {
        administrarVacunasAnchorPane.setDisable(true);
        String idVacunaSeleccionada=listaVacunas.get(Integer.valueOf(idVacuna)).getIdVacuna();
        Alert respuestaDialog=new Alert(Alert.AlertType.INFORMATION);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                listaVacunas.get(Integer.valueOf(idVacuna)).getDescripcion(),
                ButtonType.OK,ButtonType.CANCEL);
        alert.setTitle("Se eliminara la vacuna:");
        alert.setResizable(true);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarVacunaCall=mApiInterface.eliminarVacuna(SesionInfo.getClaveUsuario(),idAnimal,idVacunaSeleccionada);
            eliminarVacunaCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    respuestaDialog.setContentText(response.body().getMensaje());
                                    getListaVacunas();
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
        vacunasListView.getSelectionModel().clearSelection();
        eliminarButton.setDisable(true);
        resetInputs();
        administrarVacunasAnchorPane.setDisable(false);
    }

    //Habilitar input
    private void enableInputs(){
        descripcionTextArea.setDisable(false);
        nombreComercialTextField.setDisable(false);
        fechaVacunaDatePicker.setDisable(false);
        veterinariaTextField.setDisable(false);
    }
    //Deshabilitar input
    private void disableInputs(){
        descripcionTextArea.setDisable(true);
        nombreComercialTextField.setDisable(true);
        fechaVacunaDatePicker.setDisable(true);
        veterinariaTextField.setDisable(true);
    }


    //Volver a la vista para administrar animales
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

    //Guardar vacuna nueva
    public void guardarVacuna(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (descripcionTextArea.getText()!=null){
            if (ValidadorInputs.validarDescripcionInput(descripcionTextArea.getText())){
                if (fechaVacunaDatePicker.getValue()!=null){
                    String descripcion=descripcionTextArea.getText();
                    String fecha=fechaVacunaDatePicker.getValue().toString();
                    String nombreComercial=null;
                    String veterinaria=null;
                    if (nombreComercialTextField.getText()!=null){
                        if (ValidadorInputs.validarNombreComercialVacuna(nombreComercialTextField.getText())){
                            nombreComercial=nombreComercialTextField.getText();
                        }
                    }
                    if (veterinariaTextField.getText()!=null){
                        if (ValidadorInputs.validarVeterinaria(veterinariaTextField.getText())){
                            veterinaria=veterinariaTextField.getText();
                        }
                    }
                    Vacuna nuevaVacuna=new Vacuna(null,descripcion,nombreComercial,fecha,veterinaria,null,null);
                    Call<ApiRespuesta> agregarVacunaCall=mApiInterface.agregarVacuna(SesionInfo.getClaveUsuario(),idAnimal,nuevaVacuna);
                    agregarVacunaCall.enqueue(new Callback<ApiRespuesta>() {
                        @Override
                        public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                            Runnable runnable=()->{
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.isSuccessful()){
                                            alert.setContentText(response.body().getMensaje());
                                            getListaVacunas();
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
                    nuevaVacunaButton.setSelected(false);
                    guardarButton.setDisable(true);
                    eliminarVacunasButton.setDisable(false);
                    vacunasListView.getSelectionModel().clearSelection();
                    eliminarButton.setDisable(true);

                }else{
                    alert.setContentText("Debe indicar la fecha de aplicacion");
                    alert.show();
                }
            }else{
                alert.setContentText("Descripcion invalida");
                alert.show();
            }
        }else {
            alert.setContentText("Debe indicar una descripcion para la vacuna");
            alert.show();
        }
    }




}
