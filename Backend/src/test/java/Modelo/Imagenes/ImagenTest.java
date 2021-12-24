package Modelo.Imagenes;

import Utils.Exceptions.ExtensionDeFotoInvalidaException;
import Utils.Pair;
import Utils.Resources;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ImagenTest {

    final String directorioOrigen = "Miscelaneas/Imagenes/Imagenes-de-prueba";
    static final File directorioFinal = new File("ImagenTest");

    final String IMAGEN_GIF = "gifanimado.gif";
    final String IMAGEN_PNG = "Captura.PNG";
    final String IMAGEN_JPG_SD = "mascotas.jpg";
    final String IMAGEN_JPG_HD = "fotohd.jpg";

    @BeforeClass
    public static void init(){
        directorioFinal.mkdir();
    }

    @Test(expected = ExtensionDeFotoInvalidaException.class)
    public void siNoEsImagenRompe(){
        new Imagen(new File("ImagenTest"));
    }

    @Test
    public void noAfectaLaImagenOriginal(){
        noAfectaLaImagenOriginal(IMAGEN_GIF);
        noAfectaLaImagenOriginal(IMAGEN_JPG_HD);
        noAfectaLaImagenOriginal(IMAGEN_JPG_SD);
        noAfectaLaImagenOriginal(IMAGEN_PNG);
    }

    @Test
    public void ajustarAMismoTamanioDeImagen(){
        ajustarAMismoTamanioDeImagen(IMAGEN_GIF);
        ajustarAMismoTamanioDeImagen(IMAGEN_JPG_HD);
        ajustarAMismoTamanioDeImagen(IMAGEN_JPG_SD);
        ajustarAMismoTamanioDeImagen(IMAGEN_PNG);
    }

    @Test
    public void elTamanioSeAjustaCorrectamente(){
        Pair<Integer, Integer> tamanioArbitrario = new Pair<>(200, 1070);

        elTamanioSeAjustaCorrectamente(IMAGEN_GIF, tamanioArbitrario);
        elTamanioSeAjustaCorrectamente(IMAGEN_PNG, tamanioArbitrario);
        elTamanioSeAjustaCorrectamente(IMAGEN_JPG_HD, tamanioArbitrario);
        elTamanioSeAjustaCorrectamente(IMAGEN_JPG_SD, tamanioArbitrario);
    }

    @Test
    public void bajarLaCalidadDisminuyeElPesoYNoAlteraElTamanio(){
        bajarLaCalidadDisminuyeElPesoYNoAlteraElTamanio(IMAGEN_GIF);
        bajarLaCalidadDisminuyeElPesoYNoAlteraElTamanio(IMAGEN_JPG_HD);
        bajarLaCalidadDisminuyeElPesoYNoAlteraElTamanio(IMAGEN_JPG_SD);
    }

    @Test
    public void margenesSeCalculanCorrectamente(){
        ImagenBuilder imagen = new ImagenBuilder(getImage(IMAGEN_JPG_SD))
            .conDimensiones(50, 100);

        assertEquals(new Pair<>(2, 4), imagen.tamanioAjustadoAMargenes(4, 4));
        assertEquals(new Pair<>(50, 100), imagen.tamanioAjustadoAMargenes(100, 100));
        assertEquals(new Pair<>(25, 50), imagen.tamanioAjustadoAMargenes(25, 100));
    }

    @After
    public void clean() throws IOException {
        FileUtils.cleanDirectory(directorioFinal);
    }

    @AfterClass
    public static void delete(){
        directorioFinal.delete();
    }

//Tests ************************************************************************************

    private void noAfectaLaImagenOriginal(String imagen){
        File imagenOriginal = getImage(imagen);
        Pair<Integer, Integer> dimensionesOriginales = getDimensiones(imagenOriginal);
        long pesoOriginal = imagenOriginal.length();

        new ImagenBuilder(imagenOriginal)
            .conDimensiones(200, 470)
            .conCalidad(CalidadImagen.BASICA)
            .guardar(pathSalida());

        File imagenDespuesDeCambios = getImage(imagen);

        Pair<Integer, Integer> dimensionesActuales = getDimensiones(imagenDespuesDeCambios);
        assertEquals(pesoOriginal, imagenDespuesDeCambios.length());
        assertEquals(dimensionesOriginales, dimensionesActuales);
    }

    private void bajarLaCalidadDisminuyeElPesoYNoAlteraElTamanio(String imagen){
        File imagenOriginal = getImage(imagen);

        Imagen imagenCruda = new Imagen(imagenOriginal);

        String path = pathSalida();
        imagenCruda.guardarConCompresion(0.7f, path);
        File imagenNueva = new File(path + ".jpg");

        if(imagenOriginal.length()>20000){
            assert(imagenOriginal.length() > imagenNueva.length());
        }

        assertEquals(getDimensiones(imagenOriginal), getDimensiones(imagenNueva));
    }

    private void ajustarAMismoTamanioDeImagen(String imagen){
        elTamanioSeAjustaCorrectamente(imagen, getDimensiones(getImage(imagen)));
    }

    private void elTamanioSeAjustaCorrectamente(String imagen, Pair<Integer, Integer> dim){
        String path = pathSalida();
        new ImagenBuilder(getImage(imagen)).conDimensiones(dim.getKey(), dim.getValue()).guardar(path);

        assertEquals(dim, getDimensiones(new File(path+".jpg")));
    }

    // Auxiliares ********************************************************************
    private Pair<Integer, Integer> getDimensiones(File file){
        try{
            BufferedImage imagen = ImageIO.read(file);
            return new Pair<>(imagen.getWidth(), imagen.getHeight());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private File getImage(String imagen){
        return Resources.getFile(directorioOrigen + "/" + imagen);
    }

    private String pathSalida(){
        return directorioFinal.getPath() + "/prueba";
    }

}

