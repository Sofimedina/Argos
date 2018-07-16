
package Modelos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enfermedades {

    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("enfermedades")
    @Expose
    private List<Enfermedad> enfermedades = null;


    public Enfermedades(String estado, List<Enfermedad> enfermedades) {
        super();
        this.estado = estado;
        this.enfermedades = enfermedades;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Enfermedad> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<Enfermedad> enfermedades) {
        this.enfermedades = enfermedades;
    }

}
