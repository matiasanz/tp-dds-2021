package Repositorios;

import Modelo.Organizaciones.Organizacion;
import Repositorios.Template.RepoDB;


public class RepoOrganizaciones extends RepoDB<Organizacion> {
    public static RepoOrganizaciones instance = new RepoOrganizaciones();

    public RepoOrganizaciones(){
        super(Organizacion.class);
    }
}
