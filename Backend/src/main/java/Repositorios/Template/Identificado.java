package Repositorios.Template;

public interface Identificado {

    Long getId();

    default boolean matchId(Long id) {
        return this.getId().equals(id);
    }

    default boolean matchId(Identificable identificable){
        return matchId(identificable.getId());
    }
}
