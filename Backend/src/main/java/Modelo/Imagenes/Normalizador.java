package Modelo.Imagenes;
import Presentacion.Utils.Imagenes.ImagenDTO;
import Presentacion.Utils.ErrorHandler;
import Utils.Exceptions.EscrituraFallidaDeFotoException;
import Modelo.Organizaciones.Organizacion;
import Utils.Exceptions.LecturaFallidaDeFotoException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;

import java.io.File;


public class Normalizador {

    private final String directorio;

    public Normalizador(String carpeta){
        this.directorio = crearCarpeta(carpeta).getPath();
    }
    public String normalizar(Imagen imagen, Organizacion org){
        String ubicacion = generarNuevoPath(org);

        try{
            new ImagenBuilder(imagen)
                .conDimensiones(org.getAnchoImagenes(), org.getAltoImagenes())
                .conCalidad(org.getCalidadImagenes())
                .guardar(ubicacion);
        } catch (EscrituraFallidaDeFotoException e){
            org.notificarCancelacionDeImagen();
            throw e;
        }
        return ubicacion + "."+ Imagen.EXTENSION_SALIDA;
    }

    public String normalizar(File imagen, Organizacion org) {
        return normalizar(new Imagen(imagen), org);
    }

    private String generarNuevoPath(Organizacion org) {
        return getDirectorio(org) +"/" + org.generarIdImagen();
    }

    private String getDirectorio(Organizacion org){
        String carpeta = this.directorio +"/"+ org.getNombre();
        return crearCarpeta(carpeta).getPath();
    }

    private File crearCarpeta(String carpeta){
        File f = new File(carpeta);
        f.mkdir();

        return f;
    }
}
