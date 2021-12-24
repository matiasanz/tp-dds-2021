package Utils.Exceptions;

public class UsuarioNoExisteException extends RuntimeException {
    public UsuarioNoExisteException(String nombre){
        super("El nombre de usuario "+nombre+" no se encuentra registrado");
    }
}
