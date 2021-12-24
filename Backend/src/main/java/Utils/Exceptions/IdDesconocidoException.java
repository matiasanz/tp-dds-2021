package Utils.Exceptions;

public class IdDesconocidoException extends RuntimeException {
    public IdDesconocidoException(long id, Class<?> clase) {
        super("El id "+id+" no pertenece a ningun objeto de la clase "+ clase.getSimpleName());
    }
}
