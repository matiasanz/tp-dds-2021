package Modelo.Recomendaciones;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Organizaciones.*;
import Modelo.Publicaciones.PublicacionAdoptante;
import Repositorios.RepoOrganizaciones;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static Utils.Factory.ProveedorDeNotificaciones.*;

public class Recomendador {
    final float minimoMatcheo = 0.8f;

    RepoOrganizaciones repoOrgs;
    public Recomendador(RepoOrganizaciones repoOrgs){
        this.repoOrgs=repoOrgs;
    }

    public void execute(){
        repoOrgs.getAll().forEach(this::recomendarMascotas);
    }

    public void recomendarMascotas(Organizacion org){

        for(PublicacionAdoptante publicacion: org.getPublicacionesAdoptantes()){
            List<PublicacionAdopcion> matcheos = org
                .getMascotasEnAdopcion()
                .stream()
                .filter(mascota-> hayMatch(publicacion, mascota))
                .collect(Collectors.toList());

            if(!matcheos.isEmpty()){
                publicacion.getAspirante().enviarNotificacion(notificacionMatch(matcheos));
            }
        }
    }

    private boolean hayMatch(PublicacionAdoptante publicacion, PublicacionAdopcion mascota){
        return porcentajeDeCoincidencia(publicacion, mascota)>=minimoMatcheo;
    }

    protected float porcentajeDeCoincidencia(PublicacionAdoptante publicacion, PublicacionAdopcion mascota){
        Set<String> criterios = publicacion.getPreferencias().keySet();

        long coincidencias = criterios.stream().filter(criterio->
            publicacion.getPreferencia(criterio)
                   .equals(mascota.getRespuestaAPregunta(criterio))).count();

        return coincidencias*1f/criterios.size();
    }


}
