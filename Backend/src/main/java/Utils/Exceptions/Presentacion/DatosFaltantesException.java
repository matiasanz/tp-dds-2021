package Utils.Exceptions.Presentacion;

import Presentacion.Utils.Model.Model;

import java.util.List;

public class DatosFaltantesException extends IllegalArgumentException{
    List<String> datosFaltantes;
    public DatosFaltantesException(List<String> datosFaltantes){
        super();
        this.datosFaltantes = datosFaltantes;
    }

    @Override
    public String getMessage(){
        return new Model("datos_faltantes", datosFaltantes).toJson();
    }
}
