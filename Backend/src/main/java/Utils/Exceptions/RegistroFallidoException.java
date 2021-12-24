package Utils.Exceptions;
import java.util.List;

public abstract class RegistroFallidoException extends RuntimeException{
    public RegistroFallidoException(String mensaje) {
        super(mensaje);
    }

    public abstract List<String> getMotivos();
}
