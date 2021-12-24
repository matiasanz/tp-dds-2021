package Presentacion.Backend.Controladores.Organizaciones;

import Modelo.Imagenes.CalidadImagen;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import Modelo.Usuarios.Usuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Services.OrganizacionService;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.DimensionesNegativasException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static Presentacion.Utils.Model.Models.toJson;
import static Presentacion.Utils.RequestUtils.getBody;

public class OrganizacionesController extends OrganizacionService implements Controller {
    OrganizacionService orgService = new OrganizacionService();

    @Override
    public void routes(Service service) {
        service.get(  "/api/organizaciones", this::getOrganizaciones);
        service.get("/api/organizaciones/:idOrg", this::getOrganizacion);
        service.get(  "/api/organizaciones/:idOrg/caracteristicas-pedidas", this::getCaracteristicas);
        service.put(  "/api/organizaciones/:idOrg/imageparams", this::cambiarParametrosDeImagenes);
        service.get(  "/api/organizaciones/:idOrg/preguntas-adoptantes", this::getPreguntasAAdoptantes);
        service.post("/api/organizaciones/:idOrg/preguntas-adoptantes", this::agregarPreguntaAAdoptantes);
        service.post(  "/api/organizaciones/:idOrg/caracteristicas-pedidas", this::agregarCaracteristica);
    }

    public String getOrganizacion(Request req, Response res){
        Organizacion org = extraerOrganizacion(req);
        Model imagenes = new Model("ancho", org.getAnchoImagenes())
            .append("alto", org.getAltoImagenes())
            .append("calidad", org.getCalidadImagenes().toString());

        Optional<Usuario> usuario = usuarios.find(req);

        Model permisos = new Model()
            .append("administrador", usuario.map(org::esAdministrador).orElse(false))
            .append("voluntario", usuario.map(org::esVoluntario).orElse(false));

        return orgService.parseModel(org)
            .append("caracteristicas", org.getCaracteristicas())
            .append("preguntas_adopcion", org.getPreguntasADuenios())
            .append("imagenes", imagenes)
            .append("permisos", permisos)
            .toJson()
            ;
    }

    public String getPreguntasAAdoptantes(Request request, Response response) {
        return new Model()
            .append("propias", extraerOrganizacion(request).getPreguntasADuenios())
            .append("comunes", repoPreguntas.getPreguntasComunes())
            .toJson();
    }

    public String cambiarParametrosDeImagenes(Request request, Response response) {

        Organizacion org = extraerOrganizacion(request);

        validarPermisosAdministrador(request, org);

        try {
            Model imagenParams = getBody(request).subModel("imagenes");
            ValidadorNoNull.validar("imagenes", imagenParams);
            String calidad = imagenParams.stringValue("calidad");
            ValidadorNoNull.validar("calidad", calidad);

            withTransaction(() -> {
                org.setAnchoImagenes(imagenParams.intValue("ancho"));
                org.setAltoImagenes(imagenParams.intValue("alto"));
                org.setCalidadImagenes(CalidadImagen.valueOf(calidad));

                validarParametros(org);
            });

            return new Model().append("alto", org.getAltoImagenes()).append("ancho", org.getAltoImagenes()).append("calidad", org.getCalidadImagenes()).toJson();
        } catch (ConversionFallidaException | DatosFaltantesException | DimensionesNegativasException e) {
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        } catch (IllegalArgumentException e){
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, "La calidad debe ser uno de "+ Arrays.stream(CalidadImagen.values()).collect(Collectors.toList()));
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private void validarParametros(Organizacion org) {
        new ValidadorNoNull("imagenes")
            .conDato("ancho", org.getAnchoImagenes())
            .conDato("alto", org.getAltoImagenes())
            .conDato("calidad", org.getCalidadImagenes())
            .validar();

        if(org.getAltoImagenes()<=0 || org.getAnchoImagenes()<=0){
            throw new DimensionesNegativasException();
        }
    }

    public String getCaracteristicas(Request request, Response response) {
        List<Pregunta> caracteristicasPedidas = extraerOrganizacion(request)
            .getCaracteristicas();

        return toJson(caracteristicasPedidas);
    }

    public String getOrganizaciones(Request req, Response res) {

        List<Model> organizaciones = repoOrgs.getAll()
            .stream().map(this::parseModel).collect(Collectors.toList());
        return toJson(organizaciones);
    }

    public String agregarCaracteristica(Request req, Response res){
        return agregarPregunta(req, res, Organizacion::agregarCaracteristica);
    }

    public String agregarPreguntaAAdoptantes(Request req, Response res){
        return agregarPregunta(req, res, Organizacion::agregarPregunta);
    }

    private String agregarPregunta(Request req, Response res, BiConsumer<Organizacion, Pregunta> agregarPregunta){
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        validarPermisosAdministrador(req, org);

        try{
            Pregunta pregunta = body.classValue(Pregunta.class);
            validarPregunta(pregunta);

            withTransaction(() -> {
                agregarPregunta.accept(org, pregunta);
            });

            res.status(HttpURLConnection.HTTP_CREATED);
            return toJson(pregunta);

        } catch (ConversionFallidaException | DatosFaltantesException e){
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }
    }

    private void validarPregunta(Pregunta pregunta){
        new ValidadorNoNull()
            .conDato("detalle", pregunta.getDetalle())
            .conDato("libre_rta", pregunta.permiteLibreRespuesta())
            .validar();
    }
}
