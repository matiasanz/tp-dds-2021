package Repositorios.RepoHogares.Services;

import Modelo.Mascotas.Especie;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdmisionesParser extends TypeAdapter<List<Especie>> {

    @Override
    public List<Especie> read(JsonReader in) throws IOException {
        return new AdmisionesBuilder(in)
            .conParser("gatos",Especie.GATO)
            .conParser("perros", Especie.PERRO)
            .build()
        ;
    }

    @Override
    public void write(JsonWriter jsonWriter, List<Especie> especies) throws IOException {
        jsonWriter.beginArray();
        for (Especie e : especies){
            jsonWriter.value(e.name());
        }

        jsonWriter.endArray();
    }
}

class AdmisionesBuilder {
    List<String> admisiones = new LinkedList<>();

    Map<String, Especie> parsers = new HashMap<>();

    public AdmisionesBuilder(JsonReader reader) throws IOException {
        reader.beginObject();
        while(reader.hasNext()){
            String especie = reader.nextName();

            if(reader.nextBoolean()){
                admisiones.add(especie);
            }
        }
        reader.endObject();
    }

    public AdmisionesBuilder conParser(String nombreEspecie, Especie especie){
        parsers.put(nombreEspecie, especie);
        return this;
    }

    public List<Especie> build(){
        return admisiones.stream()
            .map(parsers::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}