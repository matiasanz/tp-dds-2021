package Repositorios;

import Modelo.Clientes.Cliente;
import Repositorios.Template.Reconocedor;
import Repositorios.Template.RepoDB;

public class RepoClientes extends RepoDB<Cliente> implements Reconocedor<Cliente> {
    public RepoClientes(){
        super(Cliente.class);
    }
}