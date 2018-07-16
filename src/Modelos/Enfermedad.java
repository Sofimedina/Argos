
package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enfermedad {

    @SerializedName("idEnfermedad")
    @Expose
    private String idEnfermedad;
    @SerializedName("cronica")
    @Expose
    private String cronica;
    @SerializedName("enfermedadAnterior")
    @Expose
    private String enfermedadAnterior;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("observaciones")
    @Expose
    private String observaciones;
    @SerializedName("Animales_idAnimal")
    @Expose
    private String animalesIdAnimal;
    @SerializedName("Animales_Usuarios_idUsuario")
    @Expose
    private String animalesUsuariosIdUsuario;


    public Enfermedad(String idEnfermedad, String cronica, String enfermedadAnterior, String descripcion, String observaciones, String animalesIdAnimal, String animalesUsuariosIdUsuario) {
        super();
        this.idEnfermedad = idEnfermedad;
        this.cronica = cronica;
        this.enfermedadAnterior = enfermedadAnterior;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.animalesIdAnimal = animalesIdAnimal;
        this.animalesUsuariosIdUsuario = animalesUsuariosIdUsuario;
    }

    public String getIdEnfermedad() {
        return idEnfermedad;
    }

    public void setIdEnfermedad(String idEnfermedad) {
        this.idEnfermedad = idEnfermedad;
    }

    public String getCronica() {
        return cronica;
    }

    public void setCronica(String cronica) {
        this.cronica = cronica;
    }

    public String getEnfermedadAnterior() {
        return enfermedadAnterior;
    }

    public void setEnfermedadAnterior(String enfermedadAnterior) {
        this.enfermedadAnterior = enfermedadAnterior;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
