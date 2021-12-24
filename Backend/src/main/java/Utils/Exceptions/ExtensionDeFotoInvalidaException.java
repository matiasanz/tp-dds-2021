package Utils.Exceptions;

public class ExtensionDeFotoInvalidaException extends RuntimeException{
    public ExtensionDeFotoInvalidaException(String extension){
        super("La extension "+extension+" no es valida para imagenes del sistema");
    }
}
