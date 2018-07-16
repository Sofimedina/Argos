package Retrofit;

import com.sun.java.browser.plugin2.DOM;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
        //TODO REVISAR IP
        public static String DOMINIO="192.168.1.17";
        //public static String DOMINIO="192.168.43.87";
        public static String BASE_URL="http://"+DOMINIO +"/api.argos.com/v1/";
        public static Retrofit retrofit=null;

        public static Retrofit getApiClient(){
            if (retrofit==null){
                retrofit=new Retrofit.Builder().
                        baseUrl(BASE_URL).
                        addConverterFactory(GsonConverterFactory.create()).
                        build();
            }
            return retrofit;
        }

        public  static void setDomain(String newDomain){
            if (DOMINIO.length() > 0) {
                DOMINIO=newDomain;
                BASE_URL="http://"+DOMINIO +"/api.argos.com/v1/";
            }
        }

    }


