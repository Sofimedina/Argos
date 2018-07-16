package Modelos;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import okhttp3.ResponseBody;

import java.io.IOException;

public class ApiRespuesta {
    @SerializedName("estado")
    private String estado;
    @SerializedName("mensaje")
    private String mensaje;


    public ApiRespuesta(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public static ApiRespuesta fromResponseBody(ResponseBody responseBody) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(responseBody.string(), ApiRespuesta.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
