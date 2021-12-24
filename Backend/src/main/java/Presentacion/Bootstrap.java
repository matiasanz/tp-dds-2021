package Presentacion;

import Modelo.Clientes.Cliente;
import Modelo.Mascotas.*;
import Modelo.MediosDeContacto.MedioNotificacion;
import Modelo.Organizaciones.Organizacion;
import Modelo.Organizaciones.Pregunta;
import Modelo.Publicaciones.EstadoValidacion;
import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Usuarios.Usuario;
import Presentacion.Backend.Controladores.Template.Controller;
import Repositorios.RepoOrganizaciones;
import Repositorios.RepoUsuarios.RepoUsuariosDB;
import Utils.Factory.ProveedorDeClientes;
import Utils.Factory.ProveedorDeMascotas;
import Utils.Factory.ProveedorDeOrganizaciones;
import Utils.Resources;
import spark.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Bootstrap implements Controller {
    private static String IMAGENES_MUESTRA = Resources.getFile("Miscelaneas/Imagenes/Imagenes-de-muestra").getPath();

    public void execute(){
        withTransaction(this::agregarOrganizaciones);
        Organizacion org = RepoOrganizaciones.instance.get(1);

        withTransaction(()-> agregarUsuarios(org));
    }

    private void agregarOrganizaciones() {
        repoOrgs.agregar(orgConDatos());

        Arrays.asList(
            "FUNDACIÓN TREWA", "PATITAS RAWSENSES", "ACTITUD ANIMAL", "4 PATAS", "FUNDAPAB", "ARAF", "ANIMALES DEL SUR", "ALUISA", "MILAGROS CANINOS"
        ).forEach(
            nombre -> repoOrgs.agregar(ProveedorDeOrganizaciones.orgConNombre(nombre))
        );
    }

    private Organizacion orgConDatos() {
        Organizacion org = ProveedorDeOrganizaciones.orgConNombreYCaracteristicas(
            "PROYECTO SUB"
            , Arrays.asList(
                new Pregunta("Está castrada", Arrays.asList("si", "no"), false)
                , new Pregunta("Es amigable", Arrays.asList("mucho","poco","nada"), true)
            )
        );

        agregarPreguntas(org);
        return org;
    }

    @Override
    public void routes(Service service) {}

    private void agregarPreguntas(Organizacion org){
        org.agregarPregunta(new Pregunta("Es pulgoso", Arrays.asList("si", "no", "maso"), false));
        repoPreguntas.agregar(new Pregunta("Le ladra a los autos", Arrays.asList("si", "no"), false) );
    }

    private void agregarUsuarios(Organizacion org){

        Cliente cliente = ProveedorDeClientes.clienteConNombre("Pablo");
        cliente.agregarMedioDeContacto(MedioNotificacion.MAIL);
        cliente.setMail("matias.l.anzorandia@gmail.com");
        Usuario admin = new Usuario("admin", "admin", cliente);
        Usuario voluntario = new Usuario("volu", "volu", ProveedorDeClientes.clienteConNombre("Joe"));

        Arrays.asList(
            admin
            , voluntario
            , new Usuario("pepe", "pepe", ProveedorDeClientes.clienteConNombre("Pedro"))
            , new Usuario("romi", "romi", ProveedorDeClientes.clienteConNombre("Romi"))
            , new Usuario("boo", "boo", ProveedorDeClientes.clienteConNombre("Boo"))
            , new Usuario("mati", "mati", ProveedorDeClientes.clienteConNombre("Mati"))
            , new Usuario("andy", "andy", ProveedorDeClientes.clienteConNombre("Andy"))
        ).forEach(new RepoUsuariosDB()::agregar);

        org.agregarAdministrador(admin);
        org.agregarVoluntario(voluntario);

        agregarMascotasPerdidas(org, admin);
        agregarMascotasRescatadas(org);
        agregarMascotasEnAdopcion(org, admin);
    }

    private void agregarMascotasPerdidas(Organizacion org, Usuario duenio){
        MascotaIdentificada gato = mascotaPerdida("Smucky", "-", "-", "ps.jpg");
        gato.setEspecie(Especie.GATO);
        Arrays.asList(
            mascotaPerdida("Humo", "-", "negro con ojos verdes y cola de pez", "perro_gris.jpg")
            , mascotaPerdida("Cujo", "-", "San Bernardo con hambre y espuma en la boca", "cujo.jpg")
            , mascotaPerdida("Betun", "-", "-", "betun.jpg")
            , mascotaSinPerder("Procer", "-", "Perro maravilla", "procer.jpg")
            ,gato
        ).forEach(m ->{
            m.setDuenio(duenio.getDatosPersonales());
            org.agregarMascota(m);
        });
    }


    private MascotaIdentificada mascotaPerdida(String nombre, String apodo, String descripcion, String foto){
        MascotaIdentificada mascotaIdentificada = mascotaSinPerder(nombre, apodo, descripcion, foto);
        mascotaIdentificada.setSituacion(SituacionMascota.PERDIDA);
        return mascotaIdentificada;
    }

    private MascotaIdentificada mascotaSinPerder(String nombre, String apodo, String descripcion, String foto){
        MascotaIdentificada mascotaIdentificada = new MascotaIdentificada(nombre, apodo, 12, SexoMascota.MACHO, descripcion, Especie.PERRO, null);
        mascotaIdentificada.agregarFoto(IMAGENES_MUESTRA+"/"+foto);
        return mascotaIdentificada;
    }

    private void agregarMascotasRescatadas(Organizacion org){
        MascotaRescatada m1 = ProveedorDeMascotas.rescatada("Se parece a Richard Gere");
        m1.agregarFoto(IMAGENES_MUESTRA+"/rg.jpg");

        MascotaRescatada m2 = ProveedorDeMascotas.rescatadaStub();
        m2.agregarFoto(IMAGENES_MUESTRA+"/pirata.jpg");

        Arrays.asList(m1, m2).forEach(org::agregarMascota);

    }

    private void agregarMascotasEnAdopcion(Organizacion org, Usuario duenio){
        MascotaIdentificada mascotaIdentificada = new MascotaIdentificada("Tornado", "-", 2, SexoMascota.MACHO, "Perro mitológico con cabeza de caballo y cuerpo de caballo", Especie.PERRO, duenio.getDatosPersonales());
        mascotaIdentificada.setSituacion(SituacionMascota.EN_CASA);
        mascotaIdentificada.agregarFoto(IMAGENES_MUESTRA+"/perrito.jpg");
        mascotaIdentificada.setSituacion(SituacionMascota.ADOPCION);
        Map<String, String> respuestasAPreguntas = new HashMap<>();
        respuestasAPreguntas.put("Color de Pelo", "Marrón");
        respuestasAPreguntas.put("Color de Ojos", "Verdes");
        PublicacionAdopcion publicacion = new PublicacionAdopcion(mascotaIdentificada,respuestasAPreguntas);
        org.agregarPublicacion(publicacion);


        mascotaIdentificada = new MascotaIdentificada("Fatiga", "Violeta", 2, SexoMascota.MACHO, "Hace honor a su nombre", Especie.PERRO, duenio.getDatosPersonales());
        mascotaIdentificada.setSituacion(SituacionMascota.EN_CASA);
        mascotaIdentificada.agregarFoto(IMAGENES_MUESTRA+"/fatiga.jpg");
        mascotaIdentificada.setSituacion(SituacionMascota.ADOPCION);
        respuestasAPreguntas = new HashMap<>();
        respuestasAPreguntas.put("Color de Pelo", "Rubio claro");
        respuestasAPreguntas.put("Color de Ojos", "Marrones");
        publicacion = new PublicacionAdopcion(mascotaIdentificada,respuestasAPreguntas);
        publicacion.setEstado(EstadoValidacion.APROBADA);
        org.agregarPublicacion(publicacion);
    }

}
