package Presentacion.Backend.Controladores;

import Modelo.Hogares.HogarDeTransito;
import Modelo.Hogares.TamanioMascota;
import Presentacion.Backend.Controladores.Template.Controller;
import Presentacion.Utils.Model.Models;
import Repositorios.RepoHogares.RepoHogares;
import Repositorios.RepoHogares.RepoHogaresMock;
import Repositorios.RepoHogares.Services.RepoHogaresRefugio;
import Utils.Exceptions.RespuestaHogaresNoOkException;
import Utils.Exceptions.TiempoDeEsperaAgotadoException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HogaresController implements Controller {
    RepoHogares repoHogares = new RepoHogaresRefugio();

    public HogaresController(){
        try{
            repoHogares.refresh();
        } catch (TiempoDeEsperaAgotadoException | RespuestaHogaresNoOkException e){
            System.out.println("\u001B[33m"+"No se pudo conectar con la api de hogares\n >> Motivo:"+e.getMessage()+"\u001B[0m");
            this.repoHogares = new RepoHogaresMock().refresh();
        }
    }

    public void routes(Service service) {
        service.get("/api/hogares", this::getHogares);
    }

    public String getHogares(Request req, Response res){
        return Models.toJson(parseModel(repoHogares.getHogares()));
    }

    private List<JsonElement> parseModel(List<HogarDeTransito> hogares){
        return hogares.stream().map(this::parseModel).collect(Collectors.toList());
    }

    private JsonElement parseModel(HogarDeTransito hogar){
        List<TamanioMascota> tamanios = Arrays.stream(TamanioMascota.values())
            .filter(hogar::admiteMascotasDeTamanio)
            .collect(Collectors.toList());

        JsonElement reader = new Gson().toJsonTree(hogar);
        reader.getAsJsonObject().add("tamanios_permitidos", new Gson().toJsonTree(tamanios));

        return reader;
    }
}
