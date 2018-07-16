package Prefs;

import Modelos.Usuario;


public class SesionInfo  {
    private static  Boolean usuarioIsLogged=false;
    private static Usuario usuarioLogueado;
    private static String claveUsuario=null;

    public static void loguearUsuario(Usuario usuario){
        usuarioIsLogged=true;
        usuarioLogueado=usuario;
        claveUsuario=usuario.getClaveApi();
    }

    public static void desconectarUsuario(){
        usuarioIsLogged=false;
        usuarioLogueado=null;
        claveUsuario=null;
    }
    public static Usuario getUsuarioLogueado(){
        return usuarioLogueado;
    }
    public static void updateUsuarioLogueado(Usuario usuario){
        usuarioLogueado=usuario;
    }
    public static String getClaveUsuario(){return claveUsuario;}

}
