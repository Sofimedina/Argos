package Api;

import Modelos.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ArgosInterface {
    //USUARIOS
    @POST("usuarios/registrar")
    Call<ApiRespuesta> registrarUsuario(@Body Usuario usuario);
    @POST("usuarios/login")
    Call<Usuario> loguearUsuario(@Body LoginBody loginBody);
    @GET("usuarios")
    Call<Usuario> leerInfoUsuario(@Header("Authorization") String claveApi);
    @PUT("usuarios")
    Call<ApiRespuesta> actualizarUsuario(@Header("Authorization") String claveApi,@Body Usuario usuario);
    @PUT("usuarios/password")
    Call<ApiRespuesta> cambiarPassword(@Header("Authorization") String claveApi,@Body Usuario usuario);
    @DELETE("usuarios")
    Call<ApiRespuesta> eliminarCuenta(@Header("Authorization") String claveApi);

    //ANIMALES
    @GET("animales")
    Call<Animales> getAnimales(@Header("Authorization") String claveApi);
    @POST("animales")
    Call<ApiRespuesta> agregarAnimal(@Header("Authorization") String claveApi,@Body Animal animal);
    @PUT("animales/{idAnimal}")
    Call<ApiRespuesta> modificarAnimal(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Body Animal animal);
    @DELETE("animales/{idAnimal}")
    Call<ApiRespuesta> eliminarAnimal(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);

    //VACUNAS
    @GET("vacunas/{idAnimal}")
    Call<Vacunas> getVacunas(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);
    @POST("vacunas/{idAnimal}")
    Call<ApiRespuesta> agregarVacuna(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Body Vacuna vacuna);
    @DELETE("vacunas/{idAnimal}/{idVacuna}")
    Call<ApiRespuesta> eliminarVacuna(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Path("idVacuna") String idVacuna);
    @DELETE("vacunas/{idAnimal}")
    Call<ApiRespuesta> eliminarTodasLasVacunas(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);

    //CIRUJIAS
    @GET("cirugias/{idAnimal}")
    Call<Cirugias> getCirugias(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);
    @POST("cirugias/{idAnimal}")
    Call<ApiRespuesta> agregarCirugia(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Body Cirugia cirugia);
    @DELETE("cirugias/{idAnimal}/{idCirugia}")
    Call<ApiRespuesta> eliminarCirugia(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Path("idCirugia") String idCirugia);
    @DELETE("cirugias/{idAnimal}")
    Call<ApiRespuesta> eliminarTodasLasCirugias(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);

    //ENFERMEDADES
    @GET("enfermedades/{idAnimal}")
    Call<Enfermedades> getEnfermedades(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);
    @POST("enfermedades/{idAnimal}")
    Call<ApiRespuesta> agregarEnfermedad(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Body Enfermedad enfermedad);
    @DELETE("enfermedades/{idAnimal}/{idEnfermedad}")
    Call<ApiRespuesta> eliminarEnfermedad(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal,@Path("idEnfermedad") String idEnfermedad);
    @DELETE("enfermedades/{idAnimal}")
    Call<ApiRespuesta> eliminarTodasLasEnfermedades(@Header("Authorization") String claveApi,@Path("idAnimal") String idAnimal);

}
