package Presentacion.Backend.Controladores;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.SituacionMascota;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.ValidadorDeRespuestas;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.ValidadorNoNull;
import Repositorios.Template.Lista;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import Utils.Exceptions.Presentacion.PreguntasException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.stream.Collectors;

import static Presentacion.Utils.ErrorHandler.haltException;
import static Presentacion.Utils.ErrorHandler.haltWithMessage;
import static Presentacion.Utils.Model.Models.toJson;
import static Presentacion.Utils.RequestUtils.getBody;

public class MisMascotasController implements Controller {

    public void routes(Service service) {
        service.post("/api/organizaciones/:idOrg/mis-mascotas", this::agregarMascota);
        service.get("/api/organizaciones/:idOrg/mis-mascotas", this::getMascotasDelCliente);
        service.get("/api/organizaciones/:idOrg/mascotas-perdidas", this::getMascotasPerdidas);
        service.post("/api/organizaciones/:idOrg/mascotas-perdidas", this::reportarMascotaPerdida);

        service.put("/api/organizaciones/:idOrg/mis-mascotas/:idMascota", this::reportarMascotaEncontrada);
    }

    private String getMascotasPerdidas(Request request, Response response) {
        Organizacion org = extraerOrganizacion(request);
        return toJson(org.getMascotasPerdidas());
    }

    private String getMascotasDelCliente(Request request, Response response) {
        Cliente cliente = extraerCliente(request);

        List<MascotaIdentificada> misMascotas = extraerOrganizacion(request)
            .getMascotasRegistradas()
            .stream().filter(m->m.getDuenio().matchId(cliente))
            .collect(Collectors.toList())
        ;

        return toJson(misMascotas);
    }

    private String agregarMascota(Request req, Response response) {
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        try {
            MascotaIdentificada mascota = body.classValue("mascota", MascotaIdentificada.class);
            validarMascota(mascota, org);

            Cliente duenio = extraerCliente(req);

            recibirFotos("fotos", org, body).forEach(mascota::agregarFoto);

            withTransaction(() -> {
                mascota.setDuenio(duenio);
                org.agregarMascota(mascota);
            });

            response.status(HttpURLConnection.HTTP_CREATED);
            return new Model("mascota", mascota).toJson();

        } catch (PreguntasException e){
            throw haltException(400, e.getMessage());
        } catch (ConversionFallidaException | DatosFaltantesException e) {
            throw haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public String reportarMascotaPerdida(Request req, Response response){
        Long idMascota = getBody(req).longValue("id_mascota");
        //TODO: Borrar las publicaciones de adopcion en este caso
        return reportarMascota(req, idMascota, SituacionMascota.PERDIDA);
    }

    public String reportarMascotaEncontrada(Request req, Response response){
        Long idMascota = Long.parseLong(req.params("idMascota"));
        return reportarMascota(req, idMascota, SituacionMascota.EN_CASA);
    }

    private String reportarMascota(Request req, Long idMascota, SituacionMascota situacion){
        Organizacion org = extraerOrganizacion(req);
        try {
            ValidadorNoNull.validar("id_mascota", idMascota);

            MascotaIdentificada mascota = Lista.findById(org.getMascotasRegistradas(), idMascota)
                .orElseThrow(()-> haltWithMessage(404, "la mascota  no existe"))
                ;

            validarDuenio(mascota, extraerCliente(req));

            if(mascota.getSituacion()==situacion){
                String situacionReportada = (situacion== SituacionMascota.PERDIDA) ? "se reporto perdida": "fue encontrada";
                throw haltWithMessage(409, "La mascota ya "+situacionReportada);
            }

            withTransaction(() -> {
                mascota.setSituacion(situacion);
            });

            return new Model("mascota", mascota).toJson();

        } catch (DatosFaltantesException|ConversionFallidaException e) {
            throw haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }
    }

    private void validarDuenio(MascotaIdentificada mascota, Cliente duenio){
        if(!duenio.matchId(mascota.getDuenio())){
            throw haltWithMessage(HttpURLConnection.HTTP_UNAUTHORIZED, "Ud. no es duenio de esta mascota");
        }
    }

    private void validarMascota(MascotaIdentificada mascota, Organizacion org) {
        new ValidadorNoNull("mascota", mascota)
            .conDato("nombre", mascota.getNombre())
            .conDato("apodo", mascota.getApodo())
            .conDato("edad_aproximada", mascota.getEdadAproximada())
            .conDato("descripcion", mascota.getDescripcion())
            .conDato("especie", mascota.getEspecie())
            .conDato("sexo", mascota.getSexo())
            .conDato("caracteristicas", mascota.getCaracteristicas())
            .conDato("situacion", mascota.getSituacion())
            .validar();

        new ValidadorDeRespuestas()
            .validar(
                org.getCaracteristicas()
                , mascota.getCaracteristicas()
            );
    }
}