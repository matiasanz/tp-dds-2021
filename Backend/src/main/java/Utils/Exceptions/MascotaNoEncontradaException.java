package Utils.Exceptions;

import Modelo.Organizaciones.Organizacion;

public class MascotaNoEncontradaException extends RuntimeException {
    public MascotaNoEncontradaException(Long id, Organizacion org) {
        super("El id "+id+" no se corresponde con el de ninguna mascota registrada en la organizacion "+ org.getNombre());
    }

    public MascotaNoEncontradaException(Long id) {
        super("El id "+id+" no se corresponde con el de ninguna mascota registrada");
    }
}
