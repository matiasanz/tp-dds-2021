package Utils.Exceptions.Presentacion;

import Modelo.Organizaciones.Pregunta;
import Presentacion.Utils.Model.Model;

import java.util.List;

public class PreguntasException extends RuntimeException {
    List<Pregunta> sinResponder;
    List<Pregunta> malRespondidas;
    List<String> noPedidas;

    public PreguntasException(List<Pregunta> sinResponder, List<Pregunta> malRespondidas, List<String> noPedidas) {
        super();
        this.sinResponder = sinResponder;
        this.malRespondidas = malRespondidas;
        this.noPedidas=noPedidas;
    }

    @Override
    public String getMessage(){
        return new Model()
            .append("sin_responder", sinResponder)
            .append("mal_respondidas", malRespondidas)
            .append("no_pedidas", noPedidas)
            .toJson();
    }
}
