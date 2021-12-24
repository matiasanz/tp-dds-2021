package Repositorios.Template;

import Modelo.Mascotas.MascotaIdentificada;

import javax.persistence.Embeddable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Lista<T extends Identificable> extends LinkedList<T>{
    public Lista(){
        super();
    }
    public Lista(List<T> elems){
        super(elems);
    }

    public static <T extends Identificable> Optional<T> findById(List<T> mascotasRegistradas, Long id) {
        return mascotasRegistradas.stream().filter(e->e.matchId(id)).findAny();
    }

    public Optional<T> findById(Long id){
        return stream().filter(e->e.matchId(id)).findAny();
    }
}
