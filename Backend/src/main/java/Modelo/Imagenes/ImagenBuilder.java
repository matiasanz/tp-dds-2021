package Modelo.Imagenes;
import Utils.Exceptions.LecturaFallidaDeFotoException;
import Utils.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagenBuilder {
    Imagen imagenCruda;
    int ancho;
    int alto;
    CalidadImagen calidad;

    public ImagenBuilder(File imagen){
        this.imagenCruda = new Imagen(imagen);
        this.ancho = imagenCruda.getAncho();
        this.alto = imagenCruda.getAlto();
    }

    public ImagenBuilder(Imagen imagenCruda){
        this.imagenCruda = imagenCruda;
        this.ancho = imagenCruda.getAncho();
        this.alto = imagenCruda.getAlto();
    }

    public ImagenBuilder conDimensiones(int ancho, int alto){
        this.ancho = ancho;
        this.alto = alto;
        return this;
    }

    public ImagenBuilder conCalidad(CalidadImagen calidad){
        this.calidad = calidad;
        return this;
    }

    public ImagenBuilder conMargenes(int ancho, int alto){
        Pair<Integer, Integer> tamanioAjustado = tamanioAjustadoAMargenes(ancho, alto);
        return conDimensiones(tamanioAjustado.getKey(), tamanioAjustado.getValue());
    }

    public void guardar(String path){
        imagenCruda.ajustarTamanio(ancho, alto);

        if(calidad!=null){
            imagenCruda.guardarConCompresion(calidad.getPorcentajeDeCompresion(), path);
        } else{
            imagenCruda.guardar(path);
        }
    }

    protected Pair<Integer, Integer> tamanioAjustadoAMargenes(int anchoMargen, int altoMargen){
        if(ancho<anchoMargen && alto< altoMargen){
            return new Pair<>(ancho, alto);
        }

        double relacionAltura = alto * 1.0/altoMargen
            ,  relacionAncho = ancho * 1.0/anchoMargen
            ,  anchoAjustado = anchoMargen
            ,  altoAjustado = altoMargen;

        if(relacionAltura>relacionAncho){
            anchoAjustado = ancho/relacionAltura;
        } else {
            altoAjustado = alto/relacionAncho;
        }

        return new Pair<>((int) anchoAjustado, (int) altoAjustado);
    }
}
