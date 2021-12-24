package Utils.Exceptions;

public class NumeroDesconocidoException extends RuntimeException {
    public NumeroDesconocidoException(Long telefono) {
        super("El numero "+telefono+" no se reconoce como telefono o no esta registrado en twilio");
    }
}
