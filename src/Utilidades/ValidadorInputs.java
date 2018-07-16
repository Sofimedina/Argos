package Utilidades;

import Modelos.Animal;
import Modelos.LoginBody;
import Modelos.Usuario;

public class ValidadorInputs {

    public static Boolean validarUsername(String username) {
        if ((username.length() >= 6) && username.length() < 60) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarPassword(String password) {
        if (password.length() >= 6 && password.length() < 60) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarEmail(String email) {
        if (email.length() >= 6 && email.length() < 255) {
            return true;
        } else {
            return false;
        }

    }

    public static Boolean validarNombre(String nombre) {
        if (nombre.length() < 60) {
            return true;
        } else {
            return false;
        }

    }

    public static Boolean validarApellido(String apellido) {
        if (apellido.length() < 60) {
            return true;
        } else {
            return false;
        }

    }

    public static Boolean validarTelefonoFijo(String telefono) {
        if (telefono.length() <= 15) {
            return true;
        } else {
            return false;
        }

    }

    public static Boolean validarCelular(String celular) {
        if (celular.length() <= 15) {
            return true;
        } else {
            return false;
        }

    }

    public static Boolean validarDireccion(String direccion) {
        if (direccion.length() < 255) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarNombreAnimal(String nombreAnimal) {
        if (nombreAnimal != null && nombreAnimal.length() < 60 && nombreAnimal.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarEspecie(String especie) {
        String[] especies = {"Perro", "Gato"};
        if (especies[0].equals(especie) || especies[1].equals(especie)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarSexo(String sexo) {
        String[] sexoOpciones = {"Hembra", "Macho"};
        if (sexoOpciones[0].equals(sexo) || sexoOpciones[1].equals(sexo)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarRaza(String raza) {
        if (raza.length() < 60) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarAños(String años) {
        if (Integer.valueOf(años) <= 20) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarMeses(String meses) {
        if (Integer.valueOf(meses) < 12) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarPeso(String peso) {
        if (Double.valueOf(peso) < 50) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarAnimalCallejero(String animalCallejero) {
        String[] opciones = {"1", "0"};
        if (opciones[0].equals(animalCallejero) || opciones[1].equals(animalCallejero)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarDesparasitado(String desparasitado) {
        String[] opciones = {"1", "0"};
        if (opciones[0].equals(desparasitado) || opciones[1].equals(desparasitado)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean validarDescripcionInput(String descripcion){
        if (descripcion.length()>0 && descripcion.length()<60){
            return true;
        }else{
            return false;
        }
    }
    public static Boolean validarNombreComercialVacuna(String nombreComercial){
        if (nombreComercial.length()<60){
            return true;
        }else{
            return false;
        }

    }
    public static Boolean validarFechaAplicacionVacuna(String fecha){
        if (fecha.length()>0){
            return true;
        }else{
            return false;
        }

    }
    public static Boolean validarVeterinaria(String veterinaria){
        if (veterinaria.length()<60){
            return true;
        }else{
            return false;
        }

    }
    public static Boolean validarObservaciones(String observaciones){
        if (observaciones.length()<255){
            return true;
        }else{
            return false;
        }

    }

    public static Boolean validarLoginInputs(LoginBody usuario) {
        if (validarUsername(usuario.getUsername()) && validarPassword(usuario.getPassword())) {
            return true;
        } else {
            return false;
        }
    }


    public static Boolean validarRegistroInputs(Usuario usuario){

        if (validarUsername(usuario.getUsername())
                &&validarPassword(usuario.getPassword())
                &&validarEmail(usuario.getEmail())
                &&validarNombre(usuario.getNombre())
                &&validarApellido(usuario.getApellido())
                &&validarTelefonoFijo(usuario.getTelefonoFijo())
                &&validarCelular(usuario.getTelefonoCelular())
                &&validarDireccion(usuario.getDireccionIndicaciones())){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean validarActualizacionInputs(Usuario usuario){

        if (validarUsername(usuario.getUsername())
                &&validarEmail(usuario.getEmail())
                &&validarNombre(usuario.getNombre())
                &&validarApellido(usuario.getApellido())
                &&validarTelefonoFijo(usuario.getTelefonoFijo())
                &&validarCelular(usuario.getTelefonoCelular())
                &&validarDireccion(usuario.getDireccionIndicaciones())){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean validarInputsAnimal(Animal animal){
        if (validarNombreAnimal(animal.getNombre())
            &&validarEspecie(animal.getEspecie())
            &&validarRaza(animal.getRaza())
            &&validarSexo(animal.getSexo())
            &&validarAños(animal.getAños())
            &&validarMeses(animal.getMeses())
            &&validarPeso(animal.getPeso())
            &&validarAnimalCallejero(animal.getAnimalCallejero())
            &&validarDesparasitado(animal.getDesparasitado())){
            return true;
        }else {
            return false;
        }
    }


}
