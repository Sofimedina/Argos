package Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("idUsuario")
    @Expose
    private String idUsuario;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("claveApi")
    @Expose
    private String claveApi;
    @SerializedName("nombre")
    @Expose
    private String nombre;
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
    @SerializedName("direccionMaps")
    @Expose
    private String direccionMaps;
    @SerializedName("direccionIndicaciones")
    @Expose
    private String direccionIndicaciones;
    @SerializedName("fotoPerfilNombre")
    @Expose
    private String fotoPerfilNombre;

    //Constructor
    public Usuario(String estado, String idUsuario, String username,String password, String email, String claveApi, String nombre, String apellido, String telefonoFijo, String telefonoCelular, String cordenadasMaps, String direccionMaps, String direccionIndicaciones, String fotoPerfilNombre) {
        super();
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.email = email;
        this.claveApi = claveApi;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefonoFijo = telefonoFijo;
        this.telefonoCelular = telefonoCelular;
        this.cordenadasMaps = cordenadasMaps;
        this.direccionMaps = direccionMaps;
        this.direccionIndicaciones = direccionIndicaciones;
        this.fotoPerfilNombre = fotoPerfilNombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClaveApi() {
        return claveApi;
    }

    public void setClaveApi(String claveApi) {
        this.claveApi = claveApi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getDireccionMaps() {
        return direccionMaps;
    }

    public void setDireccionMaps(String direccionMaps) {
        this.direccionMaps = direccionMaps;
    }

    public String getDireccionIndicaciones() {
        return direccionIndicaciones;
    }

    public void setDireccionIndicaciones(String direccionIndicaciones) {
        this.direccionIndicaciones = direccionIndicaciones;
    }

    public String getFotoPerfilNombre() {
        return fotoPerfilNombre;
    }

    public void setFotoPerfilNombre(String fotoPerfilNombre) {
        this.fotoPerfilNombre = fotoPerfilNombre;
    }

}
