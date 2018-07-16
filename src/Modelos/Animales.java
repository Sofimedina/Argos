package Modelos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Animales {

    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("animales")
    @Expose
    private List<Animal> animales = null;

    public Animales(String estado, List<Animal> animales) {
        super();
        this.estado = estado;
        this.animales = animales;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Animal> getAnimales() {
        return animales;
    }

    public void setAnimales(List<Animal> animales) {
        this.animales = animales;
    }

}
