package Utils.Exceptions;

public class PermisosYaAsignadosException extends RuntimeException {
    public PermisosYaAsignadosException(String nombre, String PERMISOS) {
        super("El usuario "+nombre+" ya tiene permisos de "+PERMISOS);
    }
}
