package Utils.Exceptions;

public class LecturaFallidaDeFotoException extends RuntimeException {
    public LecturaFallidaDeFotoException(String nombre) {
        super("Se ha producido un error de lectura al intentar normalizar el archivo "+ nombre);
    }
}
