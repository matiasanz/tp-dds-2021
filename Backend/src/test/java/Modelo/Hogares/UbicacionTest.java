package Modelo.Hogares;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UbicacionTest {
    @Test
    public void distanciaSiMeMuevoPorEjeX(){
        assertEquals(3.0, distanciaDelOrigen(3, 0), 0);
    }

    @Test
    public void distanciaSiMeMuevoPorEjeY(){
        assertEquals(4.0, distanciaDelOrigen(0, -4), 0);
    }

    @Test
    public void distanciaSiMeMuevoPorAmbosEjes(){
        assertEquals(5.0, distanciaDelOrigen(3, -4), 0);
    }

    private Double distanciaDelOrigen(int x, int y){
        return Ubicacion( 0,0).distanciaA(Ubicacion(x, y));
    }

    private Ubicacion Ubicacion(int x, int y){
        return new Ubicacion("", (double) x, (double) y);
    }
}
