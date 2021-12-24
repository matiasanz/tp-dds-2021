package Utils.Exceptions;

public class DimensionesNegativasException extends RuntimeException {
    public DimensionesNegativasException() {
        super("Las dimensiones de las imagenes no pueden ser menores a 0");
    }
}
