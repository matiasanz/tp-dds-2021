package Utils.Exceptions;

//TODO Corregir esto.
public class EscrituraFallidaDeFotoException extends RuntimeException{
    private String nombre;
    private String path;

    public EscrituraFallidaDeFotoException(String nombre, String path) {
        this.nombre=nombre;
        this.path=path;
    }

    @Override
    public String getMessage(){
        return "Se ha producido un error de escritura al intentar guardar el archivo normalizado "+ nombre + " en la ubicacion "+ path;
    }
}
