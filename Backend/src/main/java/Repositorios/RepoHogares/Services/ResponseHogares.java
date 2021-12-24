package Repositorios.RepoHogares.Services;

import Modelo.Hogares.HogarDeTransito;
import java.util.List;

public class ResponseHogares {
    private int total;
    private List<HogarDeTransito> hogares;

    public List<HogarDeTransito> getHogares() {
        return hogares;
    }

    public int getTotal(){
        return total;
    }
}
