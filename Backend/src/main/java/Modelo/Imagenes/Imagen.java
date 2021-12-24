package Modelo.Imagenes;

import Utils.Exceptions.EscrituraFallidaDeFotoException;
import Utils.Exceptions.ExtensionDeFotoInvalidaException;
import Utils.Exceptions.LecturaFallidaDeFotoException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class Imagen {
    public static final String EXTENSION_SALIDA = "jpg";

    private String nombre;
    private String extensionActual;
    private BufferedImage imagen;
    private float calidad = 1f;

    private void initialize(String nombre, String extension, Supplier<BufferedImage> getImagen){
        this.extensionActual = extension;
        validarFormato(true, "png", "jpg", "jpeg", "gif");
        this.imagen = getImagen.get();
        this.nombre = nombre;
    }

    public Imagen(String nombre, String extension, BufferedImage imagen){
        initialize(nombre, extension, ()->imagen);
    }

    public Imagen(File imagen) {
        Supplier<BufferedImage> getImagen = () -> {
            try{
                return ImageIO.read(imagen);
            } catch (IOException e) {
                throw new LecturaFallidaDeFotoException(imagen.getPath());
            }
        };

        initialize(imagen.getName(), extraerExtension(imagen), getImagen);
    }

    public int getAncho(){
        return imagen.getWidth();
    }

    public int getAlto(){
        return imagen.getHeight();
    }

    public void ajustarTamanio(int ancho, int alto) {
        BufferedImage imagenAjustada = new BufferedImage(ancho,alto, getColorType());
        Graphics2D procesadorDeGraficos = imagenAjustada.createGraphics();
        procesadorDeGraficos.drawImage(imagen, 0, 0, ancho,alto, null);
        procesadorDeGraficos.dispose(); //destructor

        imagen = imagenAjustada;
        extensionActual = "jpg";
    }

    public void guardar(String nombreSalida){
        try{
            ImageIO.write(imagen, extensionActual, new File(nombreSalida+"."+extensionActual));
        } catch (IOException e){
            //TODO: En la excepcion de
            throw new EscrituraFallidaDeFotoException(nombre, nombreSalida);
        }
    }

    public void guardarConCompresion(float calidadCompresion, String nombreSalida){
        validarFormato(false, "png");
        File archivoComprimido = new File(nombreSalida+"."+EXTENSION_SALIDA);

        try {
            ImageOutputStream borrador = ImageIO.createImageOutputStream(archivoComprimido);

            ImageWriter reescritor = ImageIO.getImageWritersByFormatName(EXTENSION_SALIDA).next();
            reescritor.setOutput(borrador);

            reescritor.write(null, new IIOImage(imagen, null, null)
                , getQualityParam(reescritor, calidadCompresion)
            );

            borrador.close();
            reescritor.dispose();

        } catch (IOException e) {
            throw new EscrituraFallidaDeFotoException(nombre, nombreSalida);
        }
    }

    //***************************** Auxiliares **********************************

    private void validarFormato(boolean valido, String ... formatos){
        if(valido == Arrays.stream(formatos).noneMatch(extensionActual::equalsIgnoreCase)) {
            throw new ExtensionDeFotoInvalidaException(extensionActual);
        }
    }


    private ImageWriteParam getQualityParam(ImageWriter writer, float calidad) {
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(calidad);

        return param;
    }

    private int getColorType(){
        return extensionActual.equalsIgnoreCase("png")?
            BufferedImage.TYPE_INT_BGR
            : imagen.getType();
    }

    public static String extraerExtension(File f){
        String path = f.getPath();
        return path.substring(path.lastIndexOf('.') + 1);
    }
}