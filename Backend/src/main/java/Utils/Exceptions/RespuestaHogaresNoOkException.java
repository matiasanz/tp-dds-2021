package Utils.Exceptions;
import org.apache.http.impl.EnglishReasonPhraseCatalog;

public class RespuestaHogaresNoOkException extends RuntimeException{
    int respuesta;

    public RespuestaHogaresNoOkException(int respuesta){
        this.respuesta = respuesta;
    }

    @Override
    public String getMessage(){
        return "Se produjo un error de tipo ["+respuesta+": "+leerRespuesta()+"] al intentar obtener los hogares de transito";
    }

    public int getRespuesta(){
        return respuesta;
    }

    public String leerRespuesta(){
        switch (respuesta){
            case 400:
                return "El parámetro \"offset\" no ha sido enviado, o es menor a 1 o excede el límite de páginas disponibles";
            case 401:
                return "No autorizado para obtener el listado";
            default:
                return EnglishReasonPhraseCatalog.INSTANCE.getReason(respuesta, null);
        }
    }
}
