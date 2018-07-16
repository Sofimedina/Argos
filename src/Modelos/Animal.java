package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Animal {
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("idAnimal")
    @Expose
    private String idAnimal;
    @SerializedName("nombre")
    @Expose
    private String nombre;
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


    public Animal(String estado, String idAnimal, String nombre, String especie, String raza, String sexo, String años, String meses, String peso, String animalCallejero, String desparasitado) {
        super();
        this.estado = estado;
        this.idAnimal = idAnimal;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.años = años;
        this.meses = meses;
        this.peso = peso;
        this.animalCallejero = animalCallejero;
        this.desparasitado = desparasitado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getAños() { return años; }

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

}
