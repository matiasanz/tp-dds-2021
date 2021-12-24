package Repositorios.RepoUsuarios;

import Modelo.Usuarios.Usuario;
import Repositorios.Template.RepoDB;

public class RepoUsuariosDB extends RepoDB<Usuario> implements RepoUsuarios{
    public RepoUsuariosDB(){
        super(Usuario.class);
    }
}
