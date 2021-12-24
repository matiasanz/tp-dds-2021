package Presentacion.Backend.Controladores;


import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Mascotas.SituacionMascota;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import Modelo.Publicaciones.PublicacionAdoptante;
import Modelo.Organizaciones.ValidadorDeRespuestas;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Repositorios.Template.Lista;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.Presentacion.PreguntasException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static Presentacion.Utils.ErrorHandler.haltException;
import static Presentacion.Utils.ErrorHandler.haltWithMessage;
import static Presentacion.Utils.Model.Models.toJson;
import static Presentacion.Utils.RequestUtils.getBody;

public class AdopcionController implements Controller {
    public void routes(Service service){
        service.get("/api/organizaciones/:idOrg/catalogo-adopcion", this::getMascotasEnAdopcion);
        service.post("/api/organizaciones/:idOrg/catalogo-adopcion", this::darEnAdopcion);
        service.post("/api/organizaciones/:idOrg/adoptantes", this::postularseAAdoptante);
    }

    private final ValidadorDeRespuestas validador = new ValidadorDeRespuestas();


    //crear publicacion para dar en adopcion una mascota mia
    public String darEnAdopcion(Request req, Response res) {
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        Long id = body.longValue("id_mascota");
        MascotaIdentificada mascota = findMascotaById(id, org)
            .orElseThrow(() -> ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, "Se intento dar en adopcion una mascota con id "+id+" inexistente")
        );

        if (!extraerCliente(req).esDuenioDe(mascota)) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, "La mascota no le pertenece");
        }

        try{
            PublicacionAdopcion publicacion = body.classValue(PublicacionAdopcion.class);
            validarRespuestas(publicacion.getRespuestasAPreguntas(), org);

            withTransaction(() -> {
                mascota.setSituacion(SituacionMascota.ADOPCION);
                publicacion.setMascota(mascota);
                org.agregarPublicacion(publicacion);
            });
            return toJson(publicacion);

        } catch (PreguntasException e){
            throw haltException(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        } catch (ConversionFallidaException e){
            throw haltWithMessage(400, e.getMessage());
        }
    }

    public String postularseAAdoptante(Request req, Response res) {
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        PublicacionAdoptante publicacion = Optional.ofNullable(body.classValue(PublicacionAdoptante.class))
            .orElse(new PublicacionAdoptante());

        validarRespuestas(publicacion.getPreferencias(), org);

        withTransaction(() -> {
            publicacion.setAspirante(extraerCliente(req));
            org.agregarPublicacion(publicacion);
        });

        res.status(HttpURLConnection.HTTP_CREATED);

        return toJson(publicacion);
    }

    private void validarRespuestas(Map<String, String> rtas, Organizacion org) {
        List<Pregunta> preguntas = repoPreguntas.getPreguntasComunes();
        preguntas.addAll(org.getPreguntasADuenios());

        try{
            validador.validar(preguntas, rtas);
        } catch (PreguntasException e){
            throw haltException(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }
    }

    private Optional<MascotaIdentificada> findMascotaById(Long id, Organizacion org){
        if(id==null) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, "No se ingreso id_mascota");
        }

        return Lista.findById(org.getMascotasRegistradas(), id);
    }

    private String getMascotasEnAdopcion(Request request, Response response) {
        List<PublicacionAdopcion> publicaciones = extraerOrganizacion(request)
            .getMascotasEnAdopcion()
            .stream().filter(m->m.getEstado().equals(EstadoValidacion.APROBADA))
            .collect(Collectors.toList());
        return toJson(publicaciones);
    }
}



