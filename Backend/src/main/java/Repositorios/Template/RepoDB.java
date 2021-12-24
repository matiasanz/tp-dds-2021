package Repositorios.Template;

import Utils.Exceptions.IdDesconocidoException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class RepoDB<T extends Identificable> {
    private Class<T> clase;
    private Transaccional db = new Transaccional() {};

    public RepoDB(Class<T> clase){
        this.clase = clase;
    }

    public List<T> getAll(){
        return entityManager()
            .createQuery("from "+clase.getSimpleName(), clase)
            .getResultList();
    }

    public void eliminar(T elem) {
        entityManager().remove(elem);
    }

    public void agregar(T elem){
        entityManager().persist(elem);
    }

    public void vaciar() {
        getAll().forEach(entityManager()::remove);
    }

    /***********************************************************/
    public T get(long id){
        return findById(id).orElseThrow(()->new IdDesconocidoException(id, clase));
    }

    public Optional<T> findById(Long id){
        T elem = entityManager().find(clase, id);
        return Optional.ofNullable(elem);
    }

    protected TypedQuery<T> createQuery(String query){
        return db.createQuery(query, clase);
    }

    protected IdDesconocidoException excepcionPorNoEncontrar(Long id){
        return new IdDesconocidoException(id, clase);
    }
    protected EntityManager entityManager(){
        return db.entityManager();
    }
}