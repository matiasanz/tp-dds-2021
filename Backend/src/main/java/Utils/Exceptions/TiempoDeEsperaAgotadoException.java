package Utils.Exceptions;

public class TiempoDeEsperaAgotadoException extends RuntimeException {
    public TiempoDeEsperaAgotadoException(){
        super("Tardó demasiado en responder");
    }
}
