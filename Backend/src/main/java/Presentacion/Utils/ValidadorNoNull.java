package Presentacion.Utils;

import Presentacion.Utils.Model.Model;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import Utils.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidadorNoNull {
    List<Pair<String, Object>> elems = new LinkedList<>();
    String prefijo = "";

    public ValidadorNoNull(){}

    public ValidadorNoNull(String contexto){
        this.prefijo = contexto + ".";
    }

    public ValidadorNoNull(String contexto, Object datoPadre){
        this(contexto);
        ValidadorNoNull.validar(contexto, datoPadre);
    }

    public static void validar(String descripcion, Object dato){
        new ValidadorNoNull().conDato(descripcion, dato).validar();
    }

    public ValidadorNoNull conDato(String descripcion, Object value) {
        elems.add(new Pair<>(prefijo+descripcion, value));
        return this;
    }

    public void validar() {
        List<String> illegalArgs = elems
            .stream()
            .filter(p -> p.getValue() == null)
            .map(Pair::getKey)
            .collect(Collectors.toList());

        if (!illegalArgs.isEmpty()) {
            throw new DatosFaltantesException(illegalArgs);
        }
    }
}
