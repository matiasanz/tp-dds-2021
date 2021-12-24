package Modelo.Organizaciones;

import Utils.Exceptions.Presentacion.PreguntasException;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


//Implementacion provisoria
public class ValidadorDeRespuestas{

    //TODO: Se podria dividir en dos clases y aca recorrer una lista de criterios y hacer directamente filter, agregando a la excepcion el map directamente
    public void validar(List<Pregunta> pedidas, Map<String, String> respuestas){
        List<Pregunta> sinResponder = invalidasSegunCriterio(pedidas, respuestas, this::sinResponder);
        List<Pregunta> malRespondidas = invalidasSegunCriterio(pedidas, respuestas, this::preguntaMalRespondida);
        List<String> noPedidas = noPedidas(pedidas, respuestas);

        if(!sinResponder.isEmpty() || !malRespondidas.isEmpty() || !noPedidas.isEmpty()) {
            throw new PreguntasException(sinResponder, malRespondidas, noPedidas);
        }
    }
    private List<Pregunta> invalidasSegunCriterio(List<Pregunta> pedidas, Map<String, String> respuestas, BiPredicate<Pregunta, Map<String, String>> validacion){
        return pedidas.stream().filter(p -> validacion.test(p, respuestas)).collect(Collectors.toList());
    }

    private Boolean sinResponder(Pregunta pregunta, Map<String, String> respuestas){
        return !respuestas.containsKey(pregunta.getDetalle());
    }

    private Boolean preguntaMalRespondida(Pregunta pregunta, Map<String, String> respuestas){
        String respuesta = respuestas.get(pregunta.getDetalle());
        return respuesta !=null && !pregunta.admiteRespuesta(respuesta);
    }

    private List<String> noPedidas(List<Pregunta> pedidas, Map<String, String> respuestas){
        return respuestas.keySet().stream().filter(p->noPedida(p, pedidas)).collect(Collectors.toList());
    }

    private boolean noPedida(String pregunta, List<Pregunta> pedidas){
        return pedidas.stream().noneMatch(p->p.getDetalle().equals(pregunta));
    }
}





