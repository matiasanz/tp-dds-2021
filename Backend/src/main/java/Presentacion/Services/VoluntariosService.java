package Presentacion.Services;

import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.Publicacion;
import Modelo.Usuarios.Usuario;
import Presentacion.Services.Template.Servicio;
import Utils.Exceptions.PublicacionInexistenteException;
import spark.Request;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VoluntariosService extends OrganizacionService {
    //***************** Service ***********************************************

    public <T extends Publicacion> T homologarPublicacion(Request req, Long id, EstadoValidacion nuevoEstado, Function<Organizacion, List<T>> extractor){
        T publicacion = getPublicacionesPendientes(req, extractor)
            .stream().filter(p->p.matchId(id)).findAny().orElseThrow(()->new PublicacionInexistenteException(id));

        withTransaction(()->{
            publicacion.setEstado(nuevoEstado);
        });

        return publicacion;
    }

    public <T extends Publicacion> List<T> getPublicacionesPendientes(Request request, Function<Organizacion, List<T>> extractor){
        Organizacion org = extraerOrganizacion(request);
        Usuario usuario = extraerUsuario(request);
        validarPermisosVoluntario(usuario, org);

        return extractor.apply(org).stream().filter(p->p.getEstado()==EstadoValidacion.PENDIENTE).collect(Collectors.toList());
    }

    private void validarPermisosVoluntario(Usuario usuario, Organizacion org){
        validarPermisos(usuario, org, Organizacion::esVoluntario);
    }
}
