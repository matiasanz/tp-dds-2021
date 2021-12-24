package Presentacion.Utils.Imagenes;
import Modelo.Imagenes.Normalizador;
import Modelo.Organizaciones.Organizacion;
import Presentacion.Utils.ErrorHandler;
import Presentacion.Utils.ValidadorNoNull;
import Utils.Exceptions.EscrituraFallidaDeFotoException;
import Utils.Exceptions.LecturaFallidaDeFotoException;
import Utils.Exceptions.Presentacion.DatosFaltantesException;
import java.util.List;
import java.util.stream.Collectors;

public class ImagenHandler {
    private Normalizador normalizador = new Normalizador("Imagenes-Normalizadas");

    public List<String> recibirImagenes(List<ImagenDTO> imagenes, Organizacion org){
        imagenes.forEach(this::validarFoto);
        try{
            return imagenes.stream()
                .map(img -> normalizar(img, org))
                .collect(Collectors.toList());

        } catch (EscrituraFallidaDeFotoException e){
            //TODO
            throw e;
        } catch (DatosFaltantesException | LecturaFallidaDeFotoException e){
            throw ErrorHandler.haltException(400, e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private void validarFoto(ImagenDTO imagenDTO) {
        new ValidadorNoNull("fotos["+imagenDTO.getNombre()+"]")
            .conDato("type", imagenDTO.getExtension())
            .conDato("name", imagenDTO.getNombre())
            .conDato("content", imagenDTO.getContent())
            .validar();
    }

    public String normalizar(ImagenDTO imagen, Organizacion org){
        return normalizador.normalizar(imagen.getAsImage(), org);
    }
}
