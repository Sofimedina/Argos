package Utilidades;

public class InfoTagReader {
    public static String puerto="COM3";

    public static void setPuerto(String nuevoPuerto){
        puerto=nuevoPuerto;
    }
    public static String getPuerto(){
        return puerto;
    }
}
