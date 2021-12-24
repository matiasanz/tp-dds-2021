package Presentacion.Backend.Controladores;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.MediosDeContacto.Notificacion;
import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.PublicacionAdopcion;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.RequestUtils;
import Presentacion.Utils.ValidadorNoNull;
import Repositorios.Template.Lista;
import Utils.Exceptions.MascotaNoEncontradaException;
import Utils.Exceptions.NotificacionFallidaException;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import Utils.Exceptions.PublicacionInexistenteException;
import Utils.Factory.ProveedorDeNotificaciones;
import spark.Request;
import spark.Response;
import spark.Service;

import java.net.HttpURLConnection;
import java.util.function.BiFunction;

import static Presentacion.Utils.Model.Models.toJson;
import static Presentacion.Utils.RequestUtils.getBody;
import static Utils.Factory.ProveedorDeNotificaciones.notificacionDeMascotaAdoptada;
import static Utils.Factory.ProveedorDeNotificaciones.notificacionMascotaEncontrada;

public class MascotasRescatadasController implements Controller {
    public void routes(Service service){
        service.get("/api/organizaciones/:idOrg/mascotas-rescatadas", this::getMascotasRescatadas);
        service.post("/api/organizaciones/:idOrg/mascotas-rescatadas", this::agregarMascotaRescatada);
    }

    public String getMascotasRescatadas(Request req, Response res){
        Organizacion org = extraerOrganizacion(req);
        return toJson(org.getMascotasRescatadas());
    }

    public String agregarMascotaRescatada(Request req, Response res){
        Model body = getBody(req);

        Organizacion org = extraerOrganizacion(req);

        try{
            MascotaRescatada mascota = body.classValue("mascota", MascotaRescatada.class);
            validarMascota(mascota);
            mascota.setFotos(recibirFotos("fotos", org, body));

            mascota.setRescatista(extraerCliente(req));

            withTransaction(()->{
                org.agregarMascota(mascota);
            });

            res.status(HttpURLConnection.HTTP_CREATED);
            return toJson(mascota);

        } catch (ConversionFallidaException | DatosFaltantesException e){
            throw ErrorHandler.haltWithMessage(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
        }

    }

    private void validarMascota(MascotaRescatada mascota) {
        new ValidadorNoNull("mascota", mascota)
            .conDato("descripcion", mascota.getDescripcionEstado())
            .conDato("ubicacion", mascota.getUbicacion())
            .validar();

        new ValidadorNoNull("mascota.ubicacion", mascota.getUbicacion())
            .conDato("lat", mascota.getUbicacion().getLatitud())
            .conDato("long", mascota.getUbicacion().getLongitud())
            .conDato("direccion", mascota.getUbicacion().getDireccion())
            .validar()
        ;
    }
}
