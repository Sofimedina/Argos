package Utilidades;

import Retrofit.ApiClient;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;

public class CheckConexion {
    public static Boolean conectarServer()  {
        try {
            //URL url=new URL("http://www.google.com");
            URL url=new URL(ApiClient.BASE_URL);
            URLConnection connection=url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
