package Utilidades;
/*
Clase helper que va a permitir decodificar las respuestas del arduino:
Inputs arduino:
r -> leer tag
w -> escribir tag
d -> borrar tag
c -> resetear tag
f -> formatear tag
t -> obtener tipo tag
Outputs:(CODIGO->ACCION-STRING):
(0000..00)->Leer Tag->No tiene informacion
(1111..11)->Leer Tag->Tag Vacio
(2222..22)->Leer Tag->No se detecto ningun tag
(01)->Escribir tag->Error con el input ingresado
(02)->No se detecto ningun tag
(03)->Escribir tag->Datos guardados
(04)->Escribir tag->Fallo la escritura del tag
(05)->Borrar tag->Tag borrado exitosamente!
(06)->Borrar tag->Fallo el intento de borrar el tag
(07)->Clean tag->Tag reseteado exitosamente!!
(08)->Clean tag->Fallo el intento de resetear el tag
(09)->Formatear tag->Tag formateado exitosamente!!
(10)->Formatear tag->Fallo el intento de formatear el tag
(11)->Get tag type->Mifare classic
)12)->Get tag type->No Mifare classic
*/
public class SolverCodigosArduino {
    public static final String E_T_NO_INFO_C="0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public static final String E_T_NO_INFO_S="Tag sin informacion!";
    public static final String E_T_EMPTY_C="1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    public static final String E_T_EMPTY_S="Tag vacio!";
    public static final String E_READING_NO_TAG_C="2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222";
    public static final String E_READING_NO_TAG_S="No se detecto ningun tag";
    public static final String E_INPUT_WRITE_C="01";
    public static final String E_INPUT_WRITE_S="Error con los datos ingresados";
    public static final String E_NO_TAG_C="02";
    public static final String E_NO_TAG_S="No se detecto ningun tag";
    public static final String S_SAVED_C="03";
    public static final String S_SAVED_S="Datos guardados!!";
    public static final String E_WRITING_TAG_C="04";
    public static final String E_WRITING_TAG_S="Fallo la escritura del tag";
    public static final String S_DELETED_TAG_C="05";
    public static final String S_DELETED_TAG_S="Tag borrado exitosamente!!";
    public static final String E_DELETING_TAG_C="06";
    public static final String E_DELETING_TAG_S="Fallo el intento de borrar el tag";
    public static final String S_CLEANED_TAG_C="07";
    public static final String S_CLEANED_TAG_S="Tag reseteado exitosamente!!";
    public static final String E_CLEANING_TAG_C="08";
    public static final String E_CLEANING_TAG_S="Fallo el intento de resetear el tag";
    public static final String S_FORMATED_TAG_C="09";
    public static final String S_FORMATED_TAG_S="Tag formateado exitosamente!!";
    public static final String E_FORMATING_TAG_C="10";
    public static final String E_FORMATING_TAG_S="Fallo el intento de formatear el tag";
    public static final String S_TYPE_MC_TAG_C="11";
    public static final String S_TYPE_MC_TAG_S="El tag es un tag Miffare Classic!";
    public static final String S_TYPE_NO_MC_TAG_C="12";
    public static final String S_TYPE_NO_MC_TAG_S="El tag  NO es un tag Miffare Classic!";

    //Metodo para interpretar las respuestas del arduino en base a los codigos definidos
    public static String traducirRespuestaArduino(String codigo){

        //Tag sin informacion
        if (codigo.equals(E_T_NO_INFO_C)){
            return E_T_NO_INFO_S;
        }
        //Tag vacio
        if (codigo.equals(E_T_EMPTY_C)){
            return E_T_EMPTY_S;
        }
        //Error con el input
        if (codigo.equals(E_INPUT_WRITE_C)){
            return E_INPUT_WRITE_S ;
        }
        if (codigo.equals(E_NO_TAG_C)){
            return E_NO_TAG_S;
        }
        if (codigo.equals(S_SAVED_C)){
            return S_SAVED_S;
        }
        if (codigo.equals(E_WRITING_TAG_C)){
            return E_WRITING_TAG_S;
        }
        if (codigo.equals(S_DELETED_TAG_C)){
            return S_DELETED_TAG_S;
        }
        if (codigo.equals(E_DELETING_TAG_C)){
            return E_DELETING_TAG_S;
        }
        if (codigo.equals(E_READING_NO_TAG_C)){
            return E_READING_NO_TAG_S;
        }

        if (codigo.equals(S_CLEANED_TAG_C)){
            return S_CLEANED_TAG_S;
        }
        if (codigo.equals(E_CLEANING_TAG_C)){
            return E_CLEANING_TAG_S;
        }
        if (codigo.equals(S_FORMATED_TAG_C)){
            return S_FORMATED_TAG_S;
        }
        if (codigo.equals(E_FORMATING_TAG_C)){
            return E_FORMATING_TAG_S;
        }
        if (codigo.equals(S_TYPE_MC_TAG_C)){
            return S_TYPE_MC_TAG_S;
        }
        if (codigo.equals(S_TYPE_NO_MC_TAG_C)){
            return S_TYPE_NO_MC_TAG_S;
        }

        return "Error desconocido";

    }

}
