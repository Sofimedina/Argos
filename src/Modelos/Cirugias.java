
package Modelos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cirugias {

    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("cirugias")
    @Expose
    private List<Cirugia> cirugias = null;


    public Cirugias(Integer estado, List<Cirugia> cirugias) {
        super();
        this.estado = estado;
        this.cirugias = cirugias;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<Cirugia> getCirugias() {
        return cirugias;
    }

    public void setCirugias(List<Cirugia> cirugias) {
        this.cirugias = cirugias;
    }

}
