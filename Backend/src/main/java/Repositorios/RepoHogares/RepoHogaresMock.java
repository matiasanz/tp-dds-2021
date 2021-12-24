package Repositorios.RepoHogares;

import Modelo.Hogares.HogarDeTransito;
import Presentacion.Utils.Model.Models;
import Utils.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RepoHogaresMock extends RepoHogares{
    private final String src= "Miscelaneas/hogares-de-transito.json";

    @Override
    public List<HogarDeTransito> refreshedHogares() {
        Type listType = new TypeToken<List<HogarDeTransito>>() {}.getType();
        return new Gson().fromJson(readFile(), listType);
    }

    private String readFile(){
        try {
            String path = Resources.getFile(src).getPath();
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("No se encontro el archivo "+src);
        }
    }
}