package Repositorios.Template;

import java.util.Optional;

public interface Reconocedor<T extends Identificable>  {
    T get(long id);
    Optional<T> findById(Long id);
}
