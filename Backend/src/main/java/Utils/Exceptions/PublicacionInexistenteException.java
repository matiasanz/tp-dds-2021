package Utils.Exceptions;

import Modelo.Organizaciones.Organizacion;

public class PublicacionInexistenteException extends RuntimeException {
    public PublicacionInexistenteException(Long id) {
        super("Se intentó homologar una publicación inexistente, con id: "+id);
    }

    public PublicacionInexistenteException(Long id, Organizacion org){
        super("Ninguna publicacion con id '"+id+"' se encuentra registrada en la organizacion '" +org.getNombre()+"'");
    }
}
