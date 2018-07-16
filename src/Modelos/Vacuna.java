
package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vacuna {

    @SerializedName("idVacuna")
    @Expose
    private String idVacuna;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("nombreComercial")
    @Expose
    private String nombreComercial;
    @SerializedName("fechaAplicacion")
    @Expose
    private String fechaAplicacion;
    @SerializedName("veterinaria")
    @Expose
    private String veterinaria;
    @SerializedName("Animales_idAnimal")
    @Expose
    private String animalesIdAnimal;
    @SerializedName("Animales_Usuarios_idUsuario")
    @Expose
    private String animalesUsuariosIdUsuario;

    
    public Vacuna(String idVacuna, String descripcion, String nombreComercial, String fechaAplicacion, String veterinaria, String animalesIdAnimal, String animalesUsuariosIdUsuario) {
        super();
        this.idVacuna = idVacuna;
        this.descripcion = descripcion;
        this.nombreComercial = nombreComercial;
        this.fechaAplicacion = fechaAplicacion;
        this.veterinaria = veterinaria;
        this.animalesIdAnimal = animalesIdAnimal;
        this.animalesUsuariosIdUsuario = animalesUsuariosIdUsuario;
    }

    public String getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(String idVacuna) {
        this.idVacuna = idVacuna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
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
