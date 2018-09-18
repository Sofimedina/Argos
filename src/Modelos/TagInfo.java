package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagInfo {
    @SerializedName("idAnimal")
    @Expose
    private String idAnimal;
    @SerializedName("telefonoCelular")
    @Expose
    private String telefonoCelular;
    @SerializedName("telefonoFijo")
    @Expose
    private String telefonoFijo;
    @SerializedName("e")
    @Expose
    private String e;

    public TagInfo(String idAnimal, String telefonoCelular, String telefonoFijo, String e) {
        super();
        this.idAnimal = idAnimal;
        this.telefonoCelular = telefonoCelular;
        this.telefonoFijo = telefonoFijo;
        this.e = e;
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }
}
