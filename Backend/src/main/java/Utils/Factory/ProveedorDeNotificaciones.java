package Utils.Factory;
import Modelo.Clientes.Cliente;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.MediosDeContacto.Notificacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProveedorDeNotificaciones {

    public static Notificacion notificacionMascotaRescatada(Cliente quienLaEncontro, MascotaRescatada mascota){
        return new Notificacion("Mascota Reconocida",
            espaciado(
               saludar(mascota.getRescatista())
                , "Ha aparecido el dueño de la mascota #"+mascota.getId()+"."
                , "Su nombre es"
                , quienLaEncontro.getNombre()
                , quienLaEncontro.getApellido()
                , "y te dejamos sus datos de contacto para que te comuniques."
                , datosDeContacto(quienLaEncontro)
            ));
    }

    public static Notificacion notificacionMascotaEncontrada(MascotaIdentificada mascota, Cliente quienLaEncontro){
        return new Notificacion("Mascota encontrada"
            , espaciado(
                saludar(mascota.getDuenio())
                , "Tu mascota ", entreComillas(mascota.getNombre()),
                "ha sido encontrada por"
                ,quienLaEncontro.getNombre()
                , quienLaEncontro.getApellido()+".\n"
                , datosDeContacto(quienLaEncontro)
            )
        );
    }

    public static Notificacion mensajeDePrueba(){
        return new Notificacion("Test Envio Mails"
            ,"Hola! Estamos probando una aplicacion. Si recibio esto, disculpe las molestias");
    }

    public static Notificacion notificacionDeMascotaAdoptada(Cliente adoptante, PublicacionAdopcion publicacionAdopcion) {
        return new Notificacion("Adopcion",
            String.join(" "
                , saludar(publicacionAdopcion.getMascotaPublicada().getDuenio())
                , adoptante.getNombre()
                , adoptante.getApellido()
                , "quiere adoptar a tu mascota"
                , publicacionAdopcion.getMascotaPublicada().getNombre()
                , ". Mandale un mail a la direccion"
                , adoptante.getMail()
                , "o llamalo al número "+adoptante.getTelefono()
            )
        );
    }

    public static Notificacion notificacionMatch(List<PublicacionAdopcion> matcheos) {
        return new Notificacion("Recomendacion semanal adopcion"
        , String.join(
            " "
                , "las siguientes mascotas se corresponden con sus preferencias"
                , matcheos.stream().map(p->p.getMascotaPublicada().getNombre()).collect(Collectors.toList()).toString()
            )
        );
    }

    public static Notificacion notificacionNuevoAdmin(){
        return new Notificacion("Nuevo admin", "Sos admin! Che, felicidades! No cualquiera...");
    }

    private static String saludar(Cliente usuario) {
        int queHoraEs = LocalDateTime.now().getHour();
        return espaciado(getSaludo(queHoraEs), usuario.getNombre() + ".");
    }

    private static String getSaludo(int queHoraEs){
        if(queHoraEs>=6 &&queHoraEs<13)
            return "Buenos días";
        else
            return "Buenas "
                + (queHoraEs<20 && queHoraEs>=13? "tardes": "noches");
    }

    private static String datosDeContacto(Cliente cliente){
        return espaciado(
            "Sus datos de contacto son:\n"
            , "• mail:", cliente.getMail()
            , "• telefono:", cliente.getTelefono().toString()
        );
    }
    private static String entreComillas(String s){

        return "\""+s+"\"";
    }


    private static String espaciado(String ... strings){
        return String.join(" ", strings);
    }
}
