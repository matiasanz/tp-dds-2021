package Presentacion.Backend;

import Presentacion.Backend.Eventos.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Request;
import spark.Response;
import spark.Service;

public interface Triggers {
    ImpresionEstado impresionEstado = new ImpresionEstado();
    HabilitadorCORS habilitador = new HabilitadorCORS();

    static void configurar(Service service){
        configurarApi("/api/*", service);

        //triggers
        service.before(Triggers::before);
        service.after(Triggers::after);
    }

    static void configurarApi(String path, Service service){
        habilitador.configurarApi(path, service);
        service.before(path, (req, res)->{
            impresionEstado.before(req, res);
            res.header("Content-Type", "application/json; charset=utf-8");
        });

        service.after(path, impresionEstado::after);
    }

    static void before(Request request, Response response) {
        response.header("Cache-Control", "no-store, must-revalidate");

    }

    static void after(Request request, Response response){
        PerThreadEntityManagers.getEntityManager();
        PerThreadEntityManagers.closeEntityManager();
    }
}
