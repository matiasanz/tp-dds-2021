package Presentacion.Utils;

import Presentacion.Backend.Eventos.ImpresionEstado;
import Presentacion.Utils.Model.Model;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;

public class ErrorHandler {

    //TODO: Usar para las preguntas/caracteristicas
    public static RuntimeException haltException(int status, String body){
        after(status, body);
        throw Spark.halt(status, body);
    }


    public static RuntimeException haltWithMessage(int status, String mensaje){
        return haltException(status, new Model("error", mensaje).toJson());
    }

    public static RuntimeException haltException(int status){
        return haltWithMessage(status, null);
    }

    private static void after(int status, String mensaje) {
        PerThreadEntityManagers.getEntityManager();
        PerThreadEntityManagers.closeEntityManager();
        ImpresionEstado.imprimirRespuesta(status, mensaje);
    }

}

