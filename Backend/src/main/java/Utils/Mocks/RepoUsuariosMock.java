package Utils.Mocks;
import Modelo.Usuarios.Usuario;
import Repositorios.RepoUsuarios.RepoUsuarios;
import Utils.Exceptions.IdDesconocidoException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class RepoUsuariosMock implements RepoUsuarios {
    private List<Usuario> usuarios = new ArrayList<>();

    public RepoUsuariosMock(){}
    public RepoUsuariosMock(List<Usuario> usuarios){
        this.usuarios.addAll(usuarios);
    }

    public List<Usuario> getAll(){
        return new LinkedList<>(usuarios);
    }

    public Usuario get(long id){
        return getAll().stream().filter(u->u.matchId(id)).findAny().orElseThrow(()->new IdDesconocidoException(id, Usuario.class))
            ;
    }

    @Override
    public Optional<Usuario> findById(Long idUsuario) {
        return Optional.empty();
    }

    public void agregar(Usuario nuevoUsuario){
        usuarios.add(nuevoUsuario);
    }

    public void setUsuarios(List<Usuario> usuarios){
        this.usuarios=usuarios;
    }
}
