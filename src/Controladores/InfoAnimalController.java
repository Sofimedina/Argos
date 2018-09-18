package Controladores;

import Api.ArgosInterface;
import Modelos.*;
import Prefs.SesionInfo;
import Retrofit.ApiClient;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InfoAnimalController implements Initializable {
    @FXML
    private ProgressIndicator infoAnimalProgressIndicator;
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
    private ListView vacunasListView;
    @FXML
    private ListView cirugiasListView;
    @FXML
    private ListView enfermedadesListView;
    @FXML
    private Label animalCallejeroLabel;
    @FXML
    private Label añosLabel;
    @FXML
    private Label mesesLabel;
    @FXML
    private Label sexoLabel;
    @FXML
    private Label pesoLabel;
    @FXML
    private Label desparasitadoLabel;
    @FXML
    private Button volverAlGestorTagsButton;
    private String  BASE_ID_ANIMAL="Id Argos:";
    private String  BASE_NOMBRE_ANIMAL="Nombre animal:";
    private String  BASE_EMAIL="Email:\n";
    private String  BASE_TELEFONO="Telefono fijo:\n";
    private String  BASE_CELULAR="Telefono celular:\n";
    private String  BASE_CORDENADAS="Cordenadas:\n";
    private String  BASE_DIRECCION="Direccion:\n";
    private String  BASE_NOMBRE_DUEÑO="Nombre del dueño:\n";
    private String  BASE_ESPECIE="Especie:";
    private String  BASE_RAZA="Raza:";
    private String  BASE_ANIMAL_CALLEJERO="Animal callejero:";
    private String  BASE_AÑOS="Años:";
    private String  BASE_MESES="Meses:";
    private String  BASE_SEXO="Sexo:";
    private String  BASE_PESO="Peso:";
    private String  BASE_DESPARASITADO="Desparasitado:";

    private ArgosInterface mApiInterface;
    private String idAnimal=null;
    private InfoDetallada infoDetallada=null;
    private List<String> listaEnfermedades=null;
    private ObservableList<String> enfermedadesObservableList=FXCollections.observableArrayList();
    private List<String> listaVacunas=null;
    private ObservableList<String> vacunasObservableList=FXCollections.observableArrayList();
    private List<String> listaCirugias=null;
    private ObservableList<String> cirugiasObservableList=FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Obtener id animal
        idAnimal=GestorTagsController.getInstance().getIdAnimal();
        //Conexion api
        mApiInterface=ApiClient.getApiClient().create(ArgosInterface.class);
        getInfoAnimal();

    }

    //Metodo para obtener info animal
    private void getInfoAnimal(){
        infoAnimalProgressIndicator.setVisible(true);
        Call<InfoDetallada> getInfoDetalladaCall=mApiInterface.getInfoDetallada(SesionInfo.getClaveUsuario(),idAnimal);
        getInfoDetalladaCall.enqueue(new Callback<InfoDetallada>() {
            @Override
            public void onResponse(Call<InfoDetallada> call, Response<InfoDetallada> response) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()){
                                infoDetallada=response.body();

                                nombreLabel.setText(BASE_NOMBRE_DUEÑO+infoDetallada.getNombreDueño());
                                emailLabel.setText(BASE_EMAIL+infoDetallada.getEmail());
                                telefonoLabel.setText(BASE_TELEFONO+infoDetallada.getTelefonoFijo());
                                celularLabel.setText(BASE_CELULAR+infoDetallada.getTelefonoCelular());
                                cordenadasLabel.setText(BASE_CORDENADAS+infoDetallada.getCordenadasMaps());
                                direccionLabel.setText(BASE_DIRECCION+infoDetallada.getDireccionIndicaciones());
                                nombreAnimalLabel.setText(BASE_NOMBRE_ANIMAL+infoDetallada.getNombreAnimal());
                                especieLabel.setText(BASE_ESPECIE+infoDetallada.getEspecie());
                                razaLabel.setText(BASE_RAZA+infoDetallada.getRaza());
                                listaVacunas=infoDetallada.getVacunas();
                                listaEnfermedades=infoDetallada.getEnfermedades();
                                listaCirugias=infoDetallada.getCirugias();
                                if (listaVacunas!=null){
                                    for (int i=0;i<listaVacunas.size();i++){
                                        vacunasObservableList.add(i,listaVacunas.get(i));
                                    }
                                    vacunasListView.setItems(vacunasObservableList);
                                }
                                if (listaCirugias!=null){
                                    for (int i=0;i<listaCirugias.size();i++){
                                        cirugiasObservableList.add(i,listaCirugias.get(i));
                                    }
                                    cirugiasListView.setItems(cirugiasObservableList);
                                }
                                if (listaEnfermedades!=null){
                                    for (int i=0;i<listaEnfermedades.size();i++){
                                        enfermedadesObservableList.add(i,listaEnfermedades.get(i));
                                    }
                                    enfermedadesListView.setItems(enfermedadesObservableList);
                                }
                                if (infoDetallada.getAnimalCallejero()!=null){
                                    if (Integer.valueOf(infoDetallada.getAnimalCallejero())==1){
                                        animalCallejeroLabel.setText(BASE_ANIMAL_CALLEJERO+"Si");
                                    }else{
                                        if (Integer.valueOf(infoDetallada.getAnimalCallejero())==0){
                                            animalCallejeroLabel.setText(BASE_ANIMAL_CALLEJERO+"No");
                                        }
                                    }
                                }

                                añosLabel.setText(BASE_AÑOS+infoDetallada.getAños());
                                mesesLabel.setText(BASE_MESES+infoDetallada.getMeses());
                                sexoLabel.setText(BASE_SEXO+infoDetallada.getSexo());
                                if (infoDetallada.getPeso()!=null){
                                    Double peso=Double.parseDouble(infoDetallada.getPeso());
                                    pesoLabel.setText(BASE_PESO+String.format("%.2f",peso));
                                }
                                if (infoDetallada.getDesparasitado()!=null){
                                    if (Integer.valueOf(infoDetallada.getDesparasitado())==1){
                                        desparasitadoLabel.setText(BASE_DESPARASITADO+"Si");
                                    }else{
                                        if (Integer.valueOf(infoDetallada.getDesparasitado())==0){
                                            desparasitadoLabel.setText(BASE_DESPARASITADO+"No");
                                        }
                                    }
                                }

                            }
                            infoAnimalProgressIndicator.setVisible(false);
                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();

            }

            @Override
            public void onFailure(Call<InfoDetallada> call, Throwable throwable) {
                Runnable runnable=()->{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            infoAnimalProgressIndicator.setVisible(false);

                        }
                    });
                };
                Thread thread=new Thread(runnable);
                thread.start();

            }
        });

    }

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
