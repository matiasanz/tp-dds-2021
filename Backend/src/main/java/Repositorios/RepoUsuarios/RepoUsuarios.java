package Repositorios.RepoUsuarios;

import Modelo.Usuarios.Usuario;
import Repositorios.Template.Reconocedor;
import Utils.Exceptions.UsuarioNoExisteException;

import java.util.List;
import java.util.Optional;

public interface RepoUsuarios extends Reconocedor<Usuario> {
    void agregar(Usuario nuevoUsuario);

    List<Usuario> getAll();

    default Usuario get(String nombre){
        return findUsuario(nombre).orElseThrow(()->new UsuarioNoExisteException(nombre));
    }

    default public boolean nombreOcupado(String nombre){
        return findUsuario(nombre).isPresent();
    }

    default Optional<Usuario> findUsuario(String nombre){
        return getAll().stream()
            .filter(u->u.getNombre().equals(nombre))
            .findAny();
    }

    default Optional<Usuario> findById(Long idUsuario){
        return getAll().stream()
            .filter(u->u.matchId(idUsuario))
            .findAny();
    }
}
