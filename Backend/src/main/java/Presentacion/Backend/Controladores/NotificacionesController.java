package Presentacion.Backend.Controladores;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.MediosDeContacto.Notificacion;
import Modelo.Organizaciones.Organizacion;
import Modelo.Publicaciones.PublicacionAdopcion;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.RequestUtils;
import Repositorios.Template.Lista;
import Utils.Exceptions.MascotaNoEncontradaException;
import Utils.Exceptions.NotificacionFallidaException;
import Utils.Exceptions.PublicacionInexistenteException;
import spark.Request;
import spark.Response;
import spark.Service;

import static Utils.Factory.ProveedorDeNotificaciones.*;

public class NotificacionesController implements Controller {

    @Override
    public void routes(Service service) {
        service.post("/api/organizaciones/:idOrg/mascotas-perdidas/:idMascota/te-encontre", this::notificarMascotaEncontrada);
        service.post("/api/organizaciones/:idOrg/catalogo-adopcion/:idPublicacion/solicitudes", this::notificarSolicitudDeAdopcion);
        service.post("/api/organizaciones/:idOrg/mascotas-rescatadas/:idMascota/te-encontre", this::notificarMascotaReconocida);
    }

    private String notificarMascotaReconocida(Request request, Response response) {
        Long idMascota = RequestUtils.getParams(request).longValue("idmascota");
        if(idMascota==null){
            throw ErrorHandler.haltWithMessage(404, "");
        }

        Organizacion org = extraerOrganizacion(request);
        Cliente cliente = extraerCliente(request);
        try {
            MascotaRescatada mascota = Lista.findById(org.getMascotasRescatadas(), idMascota)
                .orElseThrow(()->new MascotaNoEncontradaException(idMascota, org));

            Cliente duenio = mascota.getRescatista();

            return notificar(duenio, notificacionMascotaRescatada(cliente, mascota));

        } catch (MascotaNoEncontradaException e) {
            throw ErrorHandler.haltWithMessage(404, e.getMessage());
        }
    }

    private String notificarMascotaEncontrada(Request request, Response response) {
        Long idMascota = RequestUtils.getParams(request).longValue("idmascota");
        if(idMascota==null){
            throw ErrorHandler.haltWithMessage(400, "Mascota no encontrado");
        }

        Organizacion org = extraerOrganizacion(request);
        Cliente cliente = extraerCliente(request);
        try {
            MascotaIdentificada mascota = Lista.findById(org.getMascotasRegistradas(), idMascota)
                .orElseThrow(()->new MascotaNoEncontradaException(idMascota, org));

            Cliente duenio = mascota.getDuenio();

            return notificar(duenio, notificacionMascotaEncontrada(mascota, cliente));

        } catch (MascotaNoEncontradaException e) {
            throw ErrorHandler.haltWithMessage(404, e.getMessage());
        }
    }

    public String notificarSolicitudDeAdopcion(Request request, Response res){
        Long idPublicacion = RequestUtils.getParams(request).longValue("idpublicacion");

        if(idPublicacion==null){
            throw ErrorHandler.haltWithMessage(400, "");
        }

        Organizacion org = extraerOrganizacion(request);
        Cliente adoptante = extraerCliente(request);
        try {
            PublicacionAdopcion publicacion = Lista.findById(org.getMascotasEnAdopcion(), idPublicacion)
                .orElseThrow(()->new PublicacionInexistenteException(idPublicacion, org));

            Cliente duenio = publicacion.getMascotaPublicada().getDuenio();
            return notificar(duenio, notificacionDeMascotaAdoptada(adoptante, publicacion));

        } catch (PublicacionInexistenteException e) {
            throw ErrorHandler.haltWithMessage(404, e.getMessage());
        }

    }


    public String notificar(Cliente cliente, Notificacion notificacion){
        try {
            cliente.enviarNotificacion(notificacion);

            return "";

        } catch (NotificacionFallidaException e){
            throw ErrorHandler.haltException(503, "{mensaje_enviado: "+e.mensajeFueEnviado()+"}");
        }
    }

}
