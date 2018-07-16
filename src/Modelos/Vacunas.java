
package Modelos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vacunas {

    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("vacunas")
    @Expose
    private List<Vacuna> vacunas = null;

  
    public Vacunas(String estado, List<Vacuna> vacunas) {
        super();
        this.estado = estado;
        this.vacunas = vacunas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }

}
