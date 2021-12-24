package Modelo.Organizaciones;

import Modelo.Publicaciones.PublicacionAdopcion;
import Modelo.Mascotas.MascotaIdentificada;
import Modelo.Mascotas.MascotaRescatada;
import Modelo.Mascotas.SituacionMascota;
import Modelo.Publicaciones.PublicacionAdoptante;
import Modelo.Usuarios.Usuario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import Modelo.Imagenes.CalidadImagen;
import Repositorios.Template.Identificable;
import Repositorios.Template.Lista;
import Utils.Exceptions.PermisosYaAsignadosException;

import javax.persistence.*;

@Entity
@Table(name="Organizaciones")
public class Organizacion extends Identificable {
    private String nombre;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Administradores"
        , joinColumns = @JoinColumn(name = "organizacion_id")
        , inverseJoinColumns = @JoinColumn(name = "administrador_id"))
    List<Usuario> administradores = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Voluntarios"
        , joinColumns = @JoinColumn(name = "organizacion_id")
        , inverseJoinColumns = @JoinColumn(name = "voluntario_id"))
    List<Usuario> voluntarios = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "org_carac")
    List<Pregunta> caracteristicas = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    List<PublicacionAdopcion> mascotasEnAdopcion = new LinkedList<>();

    @Access(AccessType.FIELD)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    List<MascotaIdentificada> mascotasRegistradas = new LinkedList<>();

    @Access(AccessType.FIELD)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    List<MascotaRescatada> mascotasRescatadas = new LinkedList<>();

    //adopcion
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "org_preg")
    List<Pregunta> preguntasADuenios = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion")
    List<PublicacionAdoptante> publicacionesAdoptantes = new LinkedList<>();

    //normalizacion
    private Integer anchoImagenes;
    private Integer altoImagenes;

    @Enumerated(EnumType.ORDINAL)
    private CalidadImagen calidadImagenes;

    private int siguienteIdImagen = 0;

    public Organizacion() {
    }

    public Organizacion(String nombre, int anchoImagen, int altoImagen, CalidadImagen calidadImagen) {
        this.nombre = nombre;
        this.altoImagenes = altoImagen;
        this.anchoImagenes = anchoImagen;
        this.calidadImagenes = calidadImagen;
    }

    public String getNombre() {
        return nombre;
    }

    //Normalizacion
    public int getAnchoImagenes() {
        return anchoImagenes;
    }

    public int getAltoImagenes() {
        return altoImagenes;
    }

    public CalidadImagen getCalidadImagenes() {
        return calidadImagenes;
    }

    public int generarIdImagen() {
        return siguienteIdImagen++;
    }

    public void notificarCancelacionDeImagen() {
        siguienteIdImagen--;
    }

    //Administradores
    public List<Usuario> getAdministradores() {
        return administradores;
    }

    public void agregarAdministrador(Usuario nuevoAdmin) {
        if(administradores.stream().anyMatch(nuevoAdmin::matchId)){
            throw new PermisosYaAsignadosException(nuevoAdmin.getNombre(), "administrador");
        }

        administradores.add(nuevoAdmin);
    }

    //Voluntarios
    public List<Usuario> getVoluntarios() {
        return voluntarios;
    }

    public void agregarVoluntario(Usuario voluntario) {
        if(voluntarios.stream().anyMatch(voluntario::matchId)){
            throw new PermisosYaAsignadosException(voluntario.getNombre(), "voluntario");
        }

        voluntarios.add(voluntario);
    }

    public void agregarCaracteristica(Pregunta caracteristicaRequerida) {
        caracteristicas.add(caracteristicaRequerida);
    }

    public void setCaracteristicas(List<Pregunta> caracteristicas){
        this.caracteristicas = new LinkedList<>(caracteristicas);
    }

    public List<Pregunta> getCaracteristicas(){
        return caracteristicas;
    }

    //Adopcion
    public List<Pregunta> getPreguntasADuenios(){
        return preguntasADuenios;
    }

    public void agregarPregunta(Pregunta pregunta){
        preguntasADuenios.add(pregunta);
    }

    public List<PublicacionAdoptante> getPublicacionesAdoptantes() {
        return publicacionesAdoptantes;
    }

    public void agregarPublicacion(PublicacionAdoptante publicacion){
        publicacionesAdoptantes.add(publicacion);
    }

    //Mascotas
    public void agregarPublicacion(PublicacionAdopcion mascota){
        mascotasEnAdopcion.add(mascota);
    }

    public void agregarMascota(MascotaRescatada mascota){
        mascotasRescatadas.add(mascota);
    }

    public void agregarMascota(MascotaIdentificada mascota){
        mascotasRegistradas.add(mascota);
    }

    public Lista<PublicacionAdopcion> getMascotasEnAdopcion() {
        return new Lista<>(mascotasEnAdopcion);
    }

    public Lista<MascotaIdentificada> getMascotasPerdidas(){
        return new Lista<>(getMascotasRegistradas().stream().filter(m->m.getSituacion()== SituacionMascota.PERDIDA).collect(Collectors.toList()));
    }

    public List<MascotaIdentificada> getMascotasRegistradas() {
        return this.mascotasRegistradas;
    }

    public List<MascotaRescatada> getMascotasRescatadas() {
        return mascotasRescatadas;
    }


    public void setAnchoImagenes(Integer anchoImagenes) {
        this.anchoImagenes = anchoImagenes;
    }

    public void setAltoImagenes(Integer altoImagenes) {
        this.altoImagenes = altoImagenes;
    }

    public void setCalidadImagenes(CalidadImagen calidadImagenes) {
        this.calidadImagenes = calidadImagenes;
    }

    public boolean esAdministrador(Usuario usuario) {
        return getAdministradores().stream().anyMatch(usuario::matchId);
    }

    public boolean esVoluntario(Usuario usuario) {
        return getVoluntarios().stream().anyMatch(usuario::matchId);
    }
}
