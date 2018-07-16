
package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cirugia {

    @SerializedName("idCirugia")
    @Expose
    private String idCirugia;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("veterinaria")
    @Expose
    private String veterinaria;
    @SerializedName("Animales_idAnimal")
    @Expose
    private String animalesIdAnimal;
    @SerializedName("Animales_Usuarios_idUsuario")
    @Expose
    private String animalesUsuariosIdUsuario;


    public Cirugia(String idCirugia, String descripcion, String fecha, String veterinaria, String animalesIdAnimal, String animalesUsuariosIdUsuario) {
        super();
        this.idCirugia = idCirugia;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.veterinaria = veterinaria;
        this.animalesIdAnimal = animalesIdAnimal;
        this.animalesUsuariosIdUsuario = animalesUsuariosIdUsuario;
    }

    public String getIdCirugia() {
        return idCirugia;
    }

    public void setIdCirugia(String idCirugia) {
        this.idCirugia = idCirugia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getVeterinaria() {
        return veterinaria;
    }

    public void setVeterinaria(String veterinaria) {
        this.veterinaria = veterinaria;
    }

    public String getAnimalesIdAnimal() {
        return animalesIdAnimal;
    }

    public void setAnimalesIdAnimal(String animalesIdAnimal) {
        this.animalesIdAnimal = animalesIdAnimal;
    }

    public String getAnimalesUsuariosIdUsuario() {
        return animalesUsuariosIdUsuario;
    }

    public void setAnimalesUsuariosIdUsuario(String animalesUsuariosIdUsuario) {
        this.animalesUsuariosIdUsuario = animalesUsuariosIdUsuario;
    }

}
