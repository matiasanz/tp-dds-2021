package Presentacion.Backend.Eventos;

import spark.Request;
import spark.Response;
import spark.Service;

public class HabilitadorCORS {

    public void configurarApi(String path, Service service) {
        service.options(path, this::habilitarCORS);
        service.before(path, this::agregarHeader);
    }

    public void agregarHeader(Request req, Response response) {
        response.header("Access-Control-Allow-Origin", "*");
    }

    //Intercambio de recursos de origen cruzado
    public String habilitarCORS(Request request, Response response){
        permitirSiMePide("Access-Control-Request-Headers", request, response);
        permitirSiMePide("Access-Control-Request-Method", request, response);

        response.type("application/json");
        return "OK";
    }

    private void permitirSiMePide(String header, Request req, Response res){
        String recibido = req.headers(header);

        if(recibido!=null){
            String permitido = header.replace("Request", "Allow");
            res.header(permitido, recibido);
        }
    }
}
