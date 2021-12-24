package Presentacion.Backend.Eventos;

import spark.Request;
import spark.Response;

import static Presentacion.Utils.RequestUtils.*;

public class ImpresionEstado {
    public void before(Request request, Response response) {
        System.out.println("\u001B[34m"

        +"\n******** " +request.requestMethod()+ " **********"
        +"\n>> URI: "+ request.uri()
        +"\n>> Body: "+omitirFotos(getBody(request).toJson())
        +"\n>> Query params: "+getQueryParams(request).toJson()
        +"\n\n>> Session: "+ getSession(request).toJson()
        +"\n\n>> Params: "+getParams(request).toJson()
        +"\n\u001B[0m");
    }

    public void after(Request request, Response response) {
        imprimirRespuesta(response.status(), response.body());
    }

    public static void imprimirRespuesta(int status, String body){
        System.out.print( (status>=400? "\u001B[31m" : "\u001B[32m")
        +"**************** Response #"+status+ "***************"
        +"\nBody: "+omitirFotos(body) +"\n\n"
        +"\u001B[0m");
    }

    public static String omitirFotos(String s){
        String atr = "fotos";
        return s==null? "null": s.replaceAll("\""+atr+"\": ?\\Q[\\E.*?\\Q]\\E", "\""+atr+"\": 'SE OMITEN PARA IMPRESION'");
    }
}
