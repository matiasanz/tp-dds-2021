package Presentacion;

import Modelo.Hogares.HogarDeTransito;
import Modelo.Mascotas.Especie;
import Modelo.Mascotas.MascotaIdentificada;
import Presentacion.Backend.Eventos.ImpresionEstado;
import Presentacion.Utils.Model.Model;
import Presentacion.Utils.Model.Models;
import Presentacion.Utils.Model.TypeAdapters.HibernateProxyTypeAdapter;
import Presentacion.Utils.Model.TypeAdapters.LocalDateTypeAdapter;
import Repositorios.RepoHogares.RepoHogares;
import Repositorios.RepoHogares.RepoHogaresMock;
import Utils.Factory.ProveedorDeMascotas;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.dom4j.rule.Mode;
import org.junit.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ModelTest {
    @Test
    public void armoYDesarmoModeloYObtengoOriginal() {
        MascotaIdentificada ingresada = ProveedorDeMascotas.mascotaIdentificada();
        ;
        ingresada.agregarCaracteristica("pelo", "rizado");

        String json = new Model("mascota", ingresada).toJson();

        assertEquals("rizado", Models.fromJson(json).classValue("mascota", MascotaIdentificada.class).getCaracteristicas().get("pelo"));
    }

    @Test
    public void admisionesAJson() {
        HogarDeTransito hogar = new HogarDeTransito();
        hogar.setEspecies(Arrays.asList(Especie.GATO, Especie.PERRO));

        String json = Models.toJson(hogar);

        assertEquals("{\"admisiones\":[\"GATO\",\"PERRO\"]}", json);
    }

    //Typeadapters

    public static class ClaseConFecha{
        public LocalDate fecha = LocalDate.now();
    }


    @Test
    public void fechaSeLeeBien(){

        String fechaString = "2021-10-09";
        String json = "{\"fecha\":\""+fechaString+"\"}";

        Model modelo = Models.fromJson(json);

        assertEquals(
            LocalDate.parse(fechaString)
            , modelo.classValue(ClaseConFecha.class).fecha
        );
    }

/*
  @Test
    public void listaDeClasePrimitivaSeLeeBien(){
        String fechas = "{numeros: [1, 2]}";
        List<Integer> numerosLeidos = Models.fromJson(fechas).primitiveList("numeros");
        assertEquals(2, numerosLeidos.size());
        assertEquals(
            new Integer(1)
            , numerosLeidos.get(0)
        );

        assertEquals(
            new Integer(2), numerosLeidos.get(1)
        );
    }
*/
    @Test
    public void listaDeClaseNoPrimitiva(){
        MascotaIdentificada mascota = ProveedorDeMascotas.mascotaIdentificada();
        String json = "{mascotas:["+Models.toJson(mascota)+"]}";

        List<MascotaIdentificada> mascotas = Models.fromJson(json).listValue("mascotas", MascotaIdentificada.class);
        assertEquals(1, mascotas.size());
        assertEquals(mascota.getNombre(), mascotas.get(0).getNombre());
    }

    @Test
    public void parsearUnNumeroEntero(){
        assertEquals(new Integer(4), new Model("numero", "4.0")
            .intValue("numero"));

        assertEquals(new Integer(4), new Model("numero", 4.0)
            .intValue("numero"));

        assertEquals(new Integer(4), new Model("numero", 4)
            .intValue("numero"));

    }

    @Test
    public void omitoLasFotosDeUnaMascota(){
        String json = "{\"fotos\": []}";
        assertEquals("{\"fotos\": 'SE OMITEN PARA IMPRESION'}", ImpresionEstado.omitirFotos(json));
    }

    @Test
    public void omitoFotosDeListaDeMascotas(){
        String json = "[{id: 1, \"fotos\": []}, {id: 2, \"fotos\":['MANZANA', 'NARANJA']}]";
        assertEquals("[{id: 1, \"fotos\": 'SE OMITEN PARA IMPRESION'}, {id: 2, \"fotos\": 'SE OMITEN PARA IMPRESION'}]", ImpresionEstado.omitirFotos(json));
    }

    @Test
    public void hogaresMock(){
        assertEquals(40, new RepoHogaresMock().refreshedHogares().size());
    }

    @Test
    public void hogarDeJson(){
        HogarDeTransito hogarDeTransito = new Gson().fromJson(
            "{\"id\":\"eyJpdiI6IkJDUHRJcytsTnd0L3FqTmUxeEk3MVE9PSIsInZhbHVlIjoiaVdtNWZzbENpRFJ4Y0wrMVFNWVROQT09IiwibWFjIjoiYjI5M2IzNjQ4MDg1OTYwNDVmMmRiMTYzM2U5MTg3ODAyMWU0M2VjMThiMDRkMTgwNzc4YTU5NTk2ZmRkZjQ4ZCJ9\",\"nombre\":\"Pensionado de mascotas \\\"Como en casa\\\"\",\"ubicacion\":{\"direccion\":\"Av. Ing Eduardo Madero 2300, B1669BZQ Del Viso, Provincia de Buenos Aires\",\"lat\":-34.46013439745161,\"long\":-58.80857841888721},\"telefono\":\"+541164657462\",\"capacidad\":50,\"caracteristicas\":[\"Tranquilo\",\"Pacífico\"],\"lugares_disponibles\":45,\"patio\":true,\"admisiones\":{gatos: true},\"tamanios_permitidos\":[\"GRANDE\",\"MEDIANO\",\"CHICO\"]}"
            , HogarDeTransito.class
        );

        assert(hogarDeTransito.tienePatio());

    }
}
