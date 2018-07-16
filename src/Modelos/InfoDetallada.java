
package Modelos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoDetallada {

    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("nombreAnimal")
    @Expose
    private String nombreAnimal;
    @SerializedName("especie")
    @Expose
    private String especie;
    @SerializedName("raza")
    @Expose
    private String raza;
    @SerializedName("sexo")
    @Expose
    private String sexo;
    @SerializedName("años")
    @Expose
    private String años;
    @SerializedName("meses")
    @Expose
    private String meses;
    @SerializedName("peso")
    @Expose
    private String peso;
    @SerializedName("animalCallejero")
    @Expose
    private String animalCallejero;
    @SerializedName("desparasitado")
    @Expose
    private String desparasitado;
    @SerializedName("vacunas")
    @Expose
    private List<String> vacunas = null;
    @SerializedName("cirugias")
    @Expose
    private List<String> cirugias = null;
    @SerializedName("enfermedades")
    @Expose
    private List<String> enfermedades = null;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nombreDueño")
    @Expose
    private String nombreDueño;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("telefonoFijo")
    @Expose
    private String telefonoFijo;
    @SerializedName("telefonoCelular")
    @Expose
    private String telefonoCelular;
    @SerializedName("cordenadasMaps")
    @Expose
    private String cordenadasMaps;
    @SerializedName("direccionIndicaciones")
    @Expose
    private String direccionIndicaciones;

    public InfoDetallada(Integer estado, String nombreAnimal, String especie, String raza, String sexo, String años, String meses, String peso, String animalCallejero, String desparasitado, List<String> vacunas, List<String> cirugias, List<String> enfermedades, String email, String nombreDueño, String apellido, String telefonoFijo, String telefonoCelular, String cordenadasMaps, String direccionIndicaciones) {
        super();
        this.estado = estado;
        this.nombreAnimal = nombreAnimal;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.años = años;
        this.meses = meses;
        this.peso = peso;
        this.animalCallejero = animalCallejero;
        this.desparasitado = desparasitado;
        this.vacunas = vacunas;
        this.cirugias = cirugias;
        this.enfermedades = enfermedades;
        this.email = email;
        this.nombreDueño = nombreDueño;
        this.apellido = apellido;
        this.telefonoFijo = telefonoFijo;
        this.telefonoCelular = telefonoCelular;
        this.cordenadasMaps = cordenadasMaps;
        this.direccionIndicaciones = direccionIndicaciones;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getNombreAnimal() {
        return nombreAnimal;
    }

    public void setNombreAnimal(String nombreAnimal) {
        this.nombreAnimal = nombreAnimal;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getAños() {
        return años;
    }

    public void setAños(String años) {
        this.años = años;
    }

    public String getMeses() {
        return meses;
    }

    public void setMeses(String meses) {
        this.meses = meses;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAnimalCallejero() {
        return animalCallejero;
    }

    public void setAnimalCallejero(String animalCallejero) {
        this.animalCallejero = animalCallejero;
    }

    public String getDesparasitado() {
        return desparasitado;
    }

    public void setDesparasitado(String desparasitado) {
        this.desparasitado = desparasitado;
    }

    public List<String> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<String> vacunas) {
        this.vacunas = vacunas;
    }

    public List<String> getCirugias() {
        return cirugias;
    }

    public void setCirugias(List<String> cirugias) {
        this.cirugias = cirugias;
    }

    public List<String> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<String> enfermedades) {
        this.enfermedades = enfermedades;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreDueño() {
        return nombreDueño;
    }

    public void setNombreDueño(String nombreDueño) {
        this.nombreDueño = nombreDueño;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getCordenadasMaps() {
        return cordenadasMaps;
    }

    public void setCordenadasMaps(String cordenadasMaps) {
        this.cordenadasMaps = cordenadasMaps;
    }

    public String getDireccionIndicaciones() {
        return direccionIndicaciones;
    }

    public void setDireccionIndicaciones(String direccionIndicaciones) {
        this.direccionIndicaciones = direccionIndicaciones;
    }

}
