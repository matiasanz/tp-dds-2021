package Utils.Exceptions;

import Modelo.Organizaciones.Organizacion;
import Modelo.Usuarios.Usuario;

public class PermisosInsuficientesException extends RuntimeException {
    public PermisosInsuficientesException(Usuario usuario, Organizacion org, String grupo) {
        super("El usuario "+usuario.getNombre()+" intento acceder a funciones de "+grupo+", que no le correponden"
        + org.getNombre());
    }
}
