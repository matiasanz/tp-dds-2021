package Presentacion.Services.Template;
import Presentacion.Utils.ErrorHandler;
import Repositorios.Template.Identificable;
import Repositorios.Template.Reconocedor;
import spark.Request;

import java.net.HttpURLConnection;
import java.util.Optional;

import static Presentacion.Utils.RequestUtils.getSession;


public class SessionManager <T extends Identificable> {
    private String ID = "id";
    private Reconocedor<T> reconocedor;

    public SessionManager(Reconocedor<T> reconocedor){
        this.reconocedor = reconocedor;
        this.ID = reconocedor.getClass().getSimpleName().substring(4) + "_" + ID;
    }

    public T get(Request req, String mensajeEnCasoDeError){
        if(!sesionEnCurso(req)){
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_UNAUTHORIZED, mensajeEnCasoDeError);
        }

        return find(req).get();
    }


    public Optional<T> find(Request req){
        Long id = getId(req);
        return id==null? Optional.empty(): reconocedor.findById(id);
    }

    public void guardarCredenciales(T elem, Request request){
        request.session().attribute(ID, elem.getId());
    }

    private Long getId(Request req){
        return req.session().attribute(ID);
    }

    public void quitarCredenciales(Request req){
        req.session().removeAttribute(ID);
    }

    public boolean sesionEnCurso(Request req) {
        return getId(req)!=null && find(req).isPresent();
    }
}
