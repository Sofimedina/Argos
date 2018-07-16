package Controladores;

import Api.ArgosInterface;
import Modelos.Animal;
import Modelos.Animales;
import Modelos.ApiRespuesta;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import Utilidades.ValidadorInputs;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministrarAnimalesController implements Initializable {
    @FXML
    private AnchorPane administrarAnimalesAnchorPane;
    @FXML
    private ListView animalesRegistradosListView;
    @FXML
    private Button volverAlGestorTagsButton;
    @FXML
    private TextField nombreAnimalTextField;
    @FXML
    private ComboBox especieComboBox;
    @FXML
    private TextField razaTextField;
    @FXML
    private ComboBox sexoComboBox;
    @FXML
    private ChoiceBox añosChoiceBox;
    @FXML
    private ChoiceBox mesesChoiceBox;
    @FXML
    private Label pesoLabel;
    @FXML
    private Slider pesoSlider;
    @FXML
    private CheckBox animalCallejeroCheckBox;
    @FXML
    private CheckBox desparasitadoCheckBox;
    @FXML
    private Button administrarVacunasButton;
    @FXML
    private Button administrarEnfermedadesButton;
    @FXML
    private Button administrarCirugiasButton;
    @FXML
    private ToggleButton nuevoAnimalButton;
    @FXML
    private ToggleButton editarAnimalButton;
    @FXML
    private Button guardarAnimalButton;
    @FXML
    private Button borrarAnimalButton;
    @FXML
    private ProgressIndicator animalesAdmProgressIndicator;
    private ObservableList<String> animalesObservableList=FXCollections.observableArrayList();
    private ObservableList<String> sexoComboList=FXCollections.observableArrayList("Hembra","Macho");
    private ObservableList<String> especieComboList=FXCollections.observableArrayList("Perro","Gato");
    private ObservableList<Integer> meses=FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11);
    private ObservableList<Integer> años=FXCollections.observableArrayList();
    private ArgosInterface mApiInterface;
    private List<Animal> listaAnimales=null;
    private String idAnimal=null;
    private String idAnimalSeleccionado=null;

    //Instancia del controlador para enviar datos
    private static AdministrarAnimalesController instance;
    //Setear instancia del controlador
    public AdministrarAnimalesController(){
        instance=this;
    }
    //Obtener instancia del controlador
    public static AdministrarAnimalesController getInstance(){
        return instance;
    }
    //Obtener id Animal Seleccionado
    public String getIdAnimal(){
        idAnimalSeleccionado=listaAnimales.get(Integer.valueOf(idAnimal)).getIdAnimal();
        return idAnimalSeleccionado;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inicializarComponentes();
        disableInputs();
        disableButtonsNingunAnimalSeleccionado();
        guardarAnimalButton.setDisable(true);
        animalesRegistradosListView.setDisable(true);
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        getListaAnimales();

    }

    //Inicializar elementos del display
    private void inicializarComponentes(){
        //Inicializar lista de meses
        mesesChoiceBox.setItems(meses);
        //Inicializar lista de años
        for (int i=0;i<20;i++){ años.add(i);}
        añosChoiceBox.setItems( FXCollections.observableArrayList(años));
        //Setear slider para indicar el peso de un animal
        pesoLabel.setText(String.format("%.2f",pesoSlider.getValue()));
        pesoSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pesoLabel.setText(String.format("%.2f",newValue));
            }
        });
        //Inicializar choise sexo
        sexoComboBox.setItems(sexoComboList);
        //Inicializar choise especie
        especieComboBox.setItems(especieComboList);

        nuevoAnimalButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    nuevoAnimalButton.textProperty().setValue("Cancelar");
                    animalesRegistradosListView.setDisable(true);
                    animalesRegistradosListView.getSelectionModel().clearSelection();
                    disableButtonsNingunAnimalSeleccionado();
                    guardarAnimalButton.setDisable(false);
                    clearInputs();
                    enableInputs();
                }else{
                    nuevoAnimalButton.textProperty().setValue("Nuevo");
                    animalesRegistradosListView.setDisable(false);
                    disableButtonsNingunAnimalSeleccionado();
                    guardarAnimalButton.setDisable(true);
                    clearInputs();
                    disableInputs();
                }
            }
        });

        editarAnimalButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    editarAnimalButton.textProperty().setValue("Cancelar");
                    nuevoAnimalButton.setDisable(true);
                    administrarVacunasButton.setDisable(true);
                    administrarCirugiasButton.setDisable(true);
                    administrarEnfermedadesButton.setDisable(true);
                    borrarAnimalButton.setDisable(true);
                    animalesRegistradosListView.setDisable(true);
                    guardarAnimalButton.setDisable(false);
                    enableInputs();
                }else{
                    editarAnimalButton.textProperty().setValue("Editar");
                    nuevoAnimalButton.setDisable(false);
                    administrarVacunasButton.setDisable(false);
                    administrarCirugiasButton.setDisable(false);
                    administrarEnfermedadesButton.setDisable(false);
                    borrarAnimalButton.setDisable(false);
                    animalesRegistradosListView.setDisable(false);
                    guardarAnimalButton.setDisable(true);
                    disableInputs();
                }
            }
        });



    }

    //Metodo para obtener lista de animales
    private void getListaAnimales(){
        listaAnimales=null;
        animalesRegistradosListView.getItems().clear();
        animalesAdmProgressIndicator.setVisible(true);
        Call<Animales> getAnimalesCall=mApiInterface.getAnimales(SesionInfo.getClaveUsuario());
        getAnimalesCall.enqueue(new Callback<Animales>() {
            @Override
            public void onResponse(Call<Animales> call, Response<Animales> response) {
                Runnable runnableOnResponse=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            animalesAdmProgressIndicator.setVisible(false);
                            if (response.isSuccessful()){
                                listaAnimales=response.body().getAnimales();
                                for (int i=0;i<listaAnimales.size();i++){
                                    animalesObservableList.add(i,listaAnimales.get(i).getNombre());
                                }
                                animalesRegistradosListView.setItems(animalesObservableList);
                                animalesRegistradosListView.setDisable(false);
                            }
                        }
                    });

                };
                Thread thread=new Thread(runnableOnResponse);
                thread.start();
            }

            @Override
            public void onFailure(Call<Animales> call, Throwable throwable) {
                Runnable runnableOnFailure=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            animalesAdmProgressIndicator.setVisible(false);
                            Alert alert=new Alert(Alert.AlertType.ERROR,throwable.getLocalizedMessage());
                            alert.show();
                        }
                    });

                };
                Thread thread=new Thread(runnableOnFailure);
                thread.start();

            }
        });
    }

    //Metodo para desabilitar inputs
    private void disableInputs(){
        nombreAnimalTextField.setDisable(true);
        especieComboBox.setDisable(true);
        razaTextField.setDisable(true);
        sexoComboBox.setDisable(true);
        añosChoiceBox.setDisable(true);
        mesesChoiceBox.setDisable(true);
        pesoSlider.setDisable(true);
        animalCallejeroCheckBox.setDisable(true);
        desparasitadoCheckBox.setDisable(true);
    }
    //Metodo para habilitar inputs
    private void enableInputs(){
        nombreAnimalTextField.setDisable(false);
        especieComboBox.setDisable(false);
        razaTextField.setDisable(false);
        sexoComboBox.setDisable(false);
        añosChoiceBox.setDisable(false);
        mesesChoiceBox.setDisable(false);
        pesoSlider.setDisable(false);
        animalCallejeroCheckBox.setDisable(false);
        desparasitadoCheckBox.setDisable(false);
    }


    //Listener para la seleccion de la lista de animales
    public void animalSeleccionado(MouseEvent mouseEvent) {
        Integer i=animalesRegistradosListView.getSelectionModel().getSelectedIndex();
        if (i>=0) {
            idAnimal=i.toString();
            displayAnimalSeleccionado(i);
            enableButtonsAnimalSeleccionado();
        }else{
            disableButtonsNingunAnimalSeleccionado();
        }
    }

    //Metodo para poner la info del animal seleccionado en display
    private void displayAnimalSeleccionado(Integer i){
        Animal animalSeleccionado=listaAnimales.get(i);
        nombreAnimalTextField.setText(animalSeleccionado.getNombre());
        if (sexoComboList.get(0).equals(animalSeleccionado.getSexo())){
            sexoComboBox.getSelectionModel().selectFirst();
        }
        if (sexoComboList.get(1).equals(animalSeleccionado.getSexo())){
            sexoComboBox.getSelectionModel().selectLast();
        }
        if (especieComboList.get(0).equals(animalSeleccionado.getEspecie())){
            especieComboBox.getSelectionModel().selectFirst();
        }
        if (especieComboList.get(1).equals(animalSeleccionado.getEspecie())){
            especieComboBox.getSelectionModel().selectLast();
        }
        if (animalSeleccionado.getRaza()!=null){
            razaTextField.setText(listaAnimales.get(i).getRaza());
        }else{
            razaTextField.setText(null);
        }
        if (animalSeleccionado.getAños()!=null){
            Integer años=Integer.valueOf(animalSeleccionado.getAños());
            if (años<50&&años>=0){
                añosChoiceBox.getSelectionModel().select(años);
            }else{
                añosChoiceBox.getSelectionModel().clearSelection();
            }
        }else {
            añosChoiceBox.getSelectionModel().clearSelection();
        }
        if (animalSeleccionado.getMeses()!=null){
            Integer meses=Integer.valueOf(animalSeleccionado.getMeses());
            if (meses<12&&meses>=0){
                mesesChoiceBox.getSelectionModel().select(meses);
            }else{
                mesesChoiceBox.getSelectionModel().clearSelection();
            }
        }else {
            mesesChoiceBox.getSelectionModel().clearSelection();
        }

        if (animalSeleccionado.getPeso()!=null){
            pesoSlider.setValue(Double.parseDouble(animalSeleccionado.getPeso()));
        }else {
            pesoSlider.setValue(0);
        }
        if (animalSeleccionado.getAnimalCallejero()!=null){
            if (Integer.valueOf(animalSeleccionado.getAnimalCallejero())==1){
                animalCallejeroCheckBox.selectedProperty().setValue(true);
            }else {
                animalCallejeroCheckBox.selectedProperty().setValue(false);
            }
        }else{
            animalCallejeroCheckBox.selectedProperty().setValue(false);
        }

        if (animalSeleccionado.getDesparasitado()!=null){
            if (Integer.valueOf(animalSeleccionado.getDesparasitado())==1){
                desparasitadoCheckBox.selectedProperty().setValue(true);
            }else {
                desparasitadoCheckBox.selectedProperty().setValue(false);
            }
        }else{
            desparasitadoCheckBox.selectedProperty().setValue(false);
        }
    }


    //Guardar animal
    public void guardarAnimal(ActionEvent actionEvent) {
        if (editarAnimalButton.isDisable()){
          //Cargar nuevo animal
            guardarNuevoAnimal();
        }else{
           //Cargar modificaciones animal
            guardarAnimalEditado();
        }

    }



    //Guardar nuevo animal en el server
    private void guardarNuevoAnimal(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (nombreAnimalTextField.getText()!=null){
            if (ValidadorInputs.validarNombreAnimal(nombreAnimalTextField.getText())){
                if (!especieComboBox.getSelectionModel().isEmpty()){
                    if (!sexoComboBox.getSelectionModel().isEmpty()){
                        String nombre=nombreAnimalTextField.getText();
                        String especie=especieComboBox.getValue().toString();
                        String sexo=sexoComboBox.getValue().toString();
                        String raza=null;
                        String años=null;
                        String meses=null;
                        String peso=Double.toString(pesoSlider.getValue());
                        String animalCallejero=null;
                        String desparasitado=null;
                        if (razaTextField.getText()!=null){
                            if (ValidadorInputs.validarRaza(razaTextField.getText())){
                                raza=razaTextField.getText();
                            }
                        }
                        if (!añosChoiceBox.getSelectionModel().isEmpty()){
                            años=añosChoiceBox.getValue().toString();
                        }
                        if (!mesesChoiceBox.getSelectionModel().isEmpty()){
                            meses=mesesChoiceBox.getValue().toString();
                        }
                        if (animalCallejeroCheckBox.selectedProperty().getValue()){
                            animalCallejero="1";
                        }else{
                            animalCallejero="0";
                        }
                        if (desparasitadoCheckBox.selectedProperty().getValue()){
                            desparasitado="1";
                        }else{
                            desparasitado="0";
                        }
                        Animal nuevoAnimal=new Animal(null,null,nombre,especie,raza,sexo,años,
                                meses,peso,animalCallejero,desparasitado);
                        Call<ApiRespuesta> callAgregarAnimal=mApiInterface.agregarAnimal(SesionInfo.getClaveUsuario(),nuevoAnimal);
                        callAgregarAnimal.enqueue(new Callback<ApiRespuesta>() {
                            @Override
                            public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                                Runnable runnable=()->{
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (response.isSuccessful()){
                                                alert.setContentText(response.body().getMensaje());
                                            }else {
                                                ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                                alert.setContentText(error.getMensaje());
                                            }
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
                                        }
                                    });
                                };
                                Thread thread=new Thread(runnable);
                                thread.start();

                            }
                        });
                        alert.show();
                        getListaAnimales();
                        nuevoAnimalButton.setSelected(false);
                        animalesRegistradosListView.setDisable(false);
                        animalesRegistradosListView.getSelectionModel().clearSelection();
                        disableButtonsNingunAnimalSeleccionado();
                        clearInputs();
                        disableInputs();
                    }else{
                        alert.setContentText("Debe indicar el sexo del animal");
                    }

                }else{
                    alert.setContentText("Debe indicar especie");
                }

            }else{
                alert.setContentText("Nombre invalido");
            }
        }else{
            alert.setContentText("Debe ingresar un nombre");
        }
        alert.show();

    }

    //Guardar modificaciones en el server
    private void guardarAnimalEditado(){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        if (nombreAnimalTextField.getText()!=null){
            if (ValidadorInputs.validarNombreAnimal(nombreAnimalTextField.getText())){
                if (!especieComboBox.getSelectionModel().isEmpty()){
                    if (!sexoComboBox.getSelectionModel().isEmpty()){
                        String nombre=nombreAnimalTextField.getText();
                        String especie=especieComboBox.getValue().toString();
                        String sexo=sexoComboBox.getValue().toString();
                        String raza=null;
                        String años=null;
                        String meses=null;
                        String peso=Double.toString(pesoSlider.getValue());
                        String animalCallejero=null;
                        String desparasitado=null;
                        if (razaTextField.getText()!=null){
                            if (ValidadorInputs.validarRaza(razaTextField.getText())){
                                raza=razaTextField.getText();
                            }
                        }
                        if (!añosChoiceBox.getSelectionModel().isEmpty()){
                            años=añosChoiceBox.getValue().toString();
                        }
                        if (!mesesChoiceBox.getSelectionModel().isEmpty()){
                            meses=mesesChoiceBox.getValue().toString();
                        }
                        if (animalCallejeroCheckBox.selectedProperty().getValue()){
                            animalCallejero="1";
                        }else{
                            animalCallejero="0";
                        }
                        if (desparasitadoCheckBox.selectedProperty().getValue()){
                            desparasitado="1";
                        }else{
                            desparasitado="0";
                        }
                        String idAnimalSeleccionado=listaAnimales.get(Integer.valueOf(idAnimal)).getIdAnimal();
                        Animal animalEditado=new Animal(null,null,nombre,especie,raza,sexo,años,
                                meses,peso,animalCallejero,desparasitado);
                        Call<ApiRespuesta> modificarAnimalCall=mApiInterface.modificarAnimal(SesionInfo.getClaveUsuario(),idAnimalSeleccionado,animalEditado);
                        modificarAnimalCall.enqueue(new Callback<ApiRespuesta>() {
                            @Override
                            public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                                Runnable runnable=()->{
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (response.isSuccessful()){
                                                alert.setContentText(response.body().getMensaje());
                                            }else {
                                                ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                                alert.setContentText(error.getMensaje());
                                            }
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
                                        }
                                    });
                                };
                                Thread thread=new Thread(runnable);
                                thread.start();

                            }
                        });
                        alert.show();
                        getListaAnimales();
                        editarAnimalButton.setSelected(false);
                        animalesRegistradosListView.setDisable(false);
                        animalesRegistradosListView.getSelectionModel().clearSelection();
                        disableButtonsNingunAnimalSeleccionado();
                        clearInputs();
                        disableInputs();
                    }else{
                        alert.setContentText("Debe indicar el sexo del animal");
                    }

                }else{
                    alert.setContentText("Debe indicar especie");
                }
            }else{
                alert.setContentText("Nombre invalido");
            }

        }else{
            alert.setContentText("Debe ingresar un nombre");
        }
        alert.show();
    }

    //Borrar animal
    public void borrarAnimal(ActionEvent actionEvent) {
        administrarAnimalesAnchorPane.setDisable(true);
        String idAnimalSeleccionado=listaAnimales.get(Integer.valueOf(idAnimal)).getIdAnimal();
        Alert resultadoAlert=new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
        Alert alert=new Alert(Alert.AlertType.WARNING,
                "Esta seguro que desea eliminar a "+listaAnimales.get(Integer.valueOf(idAnimal)).getNombre()+"?",
                ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> result=alert.showAndWait();
        if (result.get()==ButtonType.OK){
            Call<ApiRespuesta> eliminarAnimalCall=mApiInterface.eliminarAnimal(SesionInfo.getClaveUsuario(),idAnimalSeleccionado);
            eliminarAnimalCall.enqueue(new Callback<ApiRespuesta>() {
                @Override
                public void onResponse(Call<ApiRespuesta> call, Response<ApiRespuesta> response) {
                    Runnable runnable=()->{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()){
                                    resultadoAlert.setContentText(response.body().getMensaje());
                                    getListaAnimales();
                                }else{
                                    ApiRespuesta error=ApiRespuesta.fromResponseBody(response.errorBody());
                                    resultadoAlert.setContentText(error.getMensaje());
                                }
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
                                resultadoAlert.setContentText(throwable.getLocalizedMessage());
                            }
                        });
                    };
                    Thread thread=new Thread(runnable);
                    thread.start();

                }
            });
            resultadoAlert.show();
        }
        clearInputs();
        animalesRegistradosListView.getSelectionModel().clearSelection();
        disableButtonsNingunAnimalSeleccionado();
        administrarAnimalesAnchorPane.setDisable(false);

    }

    //Metodo para borrar los datos de inputs
    private void clearInputs(){
        nombreAnimalTextField.setText(null);
        razaTextField.setText(null);
        especieComboBox.getSelectionModel().clearSelection();
        sexoComboBox.getSelectionModel().clearSelection();
        añosChoiceBox.getSelectionModel().clearSelection();
        mesesChoiceBox.getSelectionModel().clearSelection();
        pesoSlider.setValue(0);
        animalCallejeroCheckBox.selectedProperty().setValue(false);
        desparasitadoCheckBox.selectedProperty().setValue(false);
    }


    //Desabilitar cueando no haya animales seleccionados
    private void disableButtonsNingunAnimalSeleccionado(){
        borrarAnimalButton.setDisable(true);
        editarAnimalButton.setDisable(true);
        administrarVacunasButton.setDisable(true);
        administrarCirugiasButton.setDisable(true);
        administrarEnfermedadesButton.setDisable(true);
    }

    //Desabilitar cueando no haya animales seleccionados
    private void enableButtonsAnimalSeleccionado(){
        borrarAnimalButton.setDisable(false);
        editarAnimalButton.setDisable(false);
        administrarVacunasButton.setDisable(false);
        administrarCirugiasButton.setDisable(false);
        administrarEnfermedadesButton.setDisable(false);
    }

    //Volver al gestor tags
    public void launchGestorTagsView(ActionEvent actionEvent) throws IOException {
        volverAlGestorTagsButton.getScene().getWindow().hide();
        Parent parentGestorTags = FXMLLoader.load(getClass().getResource("/Vistas/GestorTagsView.fxml"));
        Scene GestorTagsScene=new Scene(parentGestorTags);
        Stage GestorTagsStage=new Stage();
        GestorTagsStage.setResizable(false);
        GestorTagsStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        GestorTagsStage.setScene(GestorTagsScene);
        GestorTagsStage.setTitle("Argos");
        GestorTagsStage.show();
    }

    //Mostrar vista para administrar vacunas
    public void launchAdministrarVacunasView(ActionEvent actionEvent) throws IOException {
        administrarVacunasButton.getScene().getWindow().hide();
        Parent parentAgregarVacuna= null;
        parentAgregarVacuna = FXMLLoader.load(getClass().getResource("/Vistas/AdministrarVacunasView.fxml"));
        Scene AgregarVacunaScene=new Scene(parentAgregarVacuna);
        Stage AgregarVacunaStage=new Stage();
        AgregarVacunaStage.setResizable(false);
        AgregarVacunaStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AgregarVacunaStage.setScene(AgregarVacunaScene);
        AgregarVacunaStage.setTitle("Vacunas");
        AgregarVacunaStage.show();
    }

    //Mostrar vista para agregar enfermedad
    public void launchAdministrarEnfermedadesView(ActionEvent actionEvent) throws IOException {
        administrarEnfermedadesButton.getScene().getWindow().hide();
        Parent parentAgregarEnfermedad= null;
        parentAgregarEnfermedad = FXMLLoader.load(getClass().getResource("/Vistas/AdministrarEnfermedadesView.fxml"));
        Scene AgregarEnfermedadScene=new Scene(parentAgregarEnfermedad);
        Stage AgregarEnfermedadStage=new Stage();
        AgregarEnfermedadStage.setResizable(false);
        AgregarEnfermedadStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AgregarEnfermedadStage.setScene(AgregarEnfermedadScene);
        AgregarEnfermedadStage.setTitle("Enfermedades");
        AgregarEnfermedadStage.show();
    }

    //Mostrar vista para administrar cirugias
    public void launchAdministrarCirugiasView(ActionEvent actionEvent) throws IOException {
        administrarCirugiasButton.getScene().getWindow().hide();
        Parent parentAgregarCirugia= null;
        parentAgregarCirugia = FXMLLoader.load(getClass().getResource("/Vistas/AdministrarCirugiasView.fxml"));
        Scene AgregarCirugiaScene=new Scene(parentAgregarCirugia);
        Stage AgregarCirugiaStage=new Stage();
        AgregarCirugiaStage.setResizable(false);
        AgregarCirugiaStage.getIcons().add(new Image("/Imgs/IconV32.png"));
        AgregarCirugiaStage.setScene(AgregarCirugiaScene);
        AgregarCirugiaStage.setTitle("Cirugias");
        AgregarCirugiaStage.show();
    }
}
