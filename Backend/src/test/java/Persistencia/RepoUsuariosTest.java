package Persistencia;

import Modelo.Usuarios.Usuario;
import Repositorios.RepoUsuarios.RepoUsuariosDB;
import Repositorios.Template.Transaccional;
import Utils.Exceptions.UsuarioNoExisteException;
import Utils.Factory.ProveedorDeClientes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepoUsuariosTest implements Transaccional {
    RepoUsuariosDB repo = new RepoUsuariosDB();
    Usuario unUsuario = new Usuario("beto", "a la grande le puse cuca", ProveedorDeClientes.clienteConNombre("Norberto"));

    @Before
    public void init(){
        withTransaction(()->{
            repo.agregar(unUsuario);
        });
    }

    @After
    public void clean(){
        withTransaction(()->{
            repo.eliminar(unUsuario);
        });
    }

    @Test
    public void usuarioSeInsertoCorrectamente(){
        assertNotNull(unUsuario.getId());
    }

    @Test
    public void usuarioSeObtienePorNombre(){
        Usuario leido = repo.get(unUsuario.getNombre());
        assertEquals(unUsuario.getId(), leido.getId());
    }

    @Test
    public void elNombreDeUnUsuarioExistenteEstaOcupado(){
        assert (repo.nombreOcupado(unUsuario.getNombre()));
    }

    @Test(expected = UsuarioNoExisteException.class)
    public void siUsuarioNoExisteArrojaExcepcion(){
        repo.get("Mark Knopfler");
    }

    @Test
    public void elNombreDeUnUsuarioInexistenteNoEstaOcupado(){
        assertFalse(repo.nombreOcupado("Mark Knopfler"));
    }
}
