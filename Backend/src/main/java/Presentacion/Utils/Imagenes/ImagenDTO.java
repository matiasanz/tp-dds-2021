package Presentacion.Utils.Imagenes;

import Modelo.Imagenes.Imagen;
import Modelo.Imagenes.ImagenBuilder;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.Model.Models;
import Utils.Exceptions.LecturaFallidaDeFotoException;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ImagenDTO {
    @SerializedName("content")
    private String base64;
    @SerializedName("type")
    private String extension;
    @SerializedName("name")
    private String nombre;

    public ImagenDTO(){}
    public ImagenDTO(String path){
        File imagen = new File(path);
        this.extension = Imagen.extraerExtension(imagen);
        this.nombre=imagen.getName();
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(imagen);
            this.base64 = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new LecturaFallidaDeFotoException(path);
        }
    }

    public Imagen getAsImage(){
        return new Imagen(nombre, extension, getAsBufferedImage());
    }

    public BufferedImage getAsBufferedImage() {
        try{
            return ImageIO.read(getAsByteArray(base64));
        } catch (IOException e){
            throw new LecturaFallidaDeFotoException(nombre);
        }
    }

    private ByteArrayInputStream getAsByteArray(String base64){
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64);
        return new ByteArrayInputStream(imageBytes);
    }

    public String getExtension() {
        return extension;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContent(){
        return base64;
    }

}