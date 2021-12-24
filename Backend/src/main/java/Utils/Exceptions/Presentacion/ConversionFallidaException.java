package Utils.Exceptions.Presentacion;

import Presentacion.Utils.Model.Model;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

public class ConversionFallidaException extends RuntimeException {
    public ConversionFallidaException(JsonSyntaxException e) {
        super("Sintaxis incorrecta en json"); //TODO customizar
        e.printStackTrace();
    }

    public ConversionFallidaException(Exception e, String queCosa){
        super("Se produjo un error al intentar leer "+ queCosa);
    }

    public ConversionFallidaException(String key, Object value){
        super(
            new Model("dato_incompatible"
                , new Model("nombre", key).append("valor", value)
            ).toJson()
        );
    }
}
