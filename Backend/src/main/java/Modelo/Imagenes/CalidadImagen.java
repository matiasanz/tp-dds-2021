package Modelo.Imagenes;

public enum CalidadImagen{

    BUENA(0.9f)
    , NORMAL(0.8f)
    , BASICA(0.7f);

    CalidadImagen(float porcentajeDeCompresion){
        this.porcentajeDeCompresion = porcentajeDeCompresion;
    }

    private final float porcentajeDeCompresion;
    public float getPorcentajeDeCompresion(){
        return porcentajeDeCompresion;
    }
}
