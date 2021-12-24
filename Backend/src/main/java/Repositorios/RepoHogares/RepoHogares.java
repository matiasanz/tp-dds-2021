package Repositorios.RepoHogares;

import Modelo.Hogares.HogarDeTransito;

import java.util.List;

public abstract class RepoHogares {
    protected List<HogarDeTransito> hogares;

    public List<HogarDeTransito> getHogares(){
        return this.hogares;
    }

    public RepoHogares refresh(){
        this.hogares = refreshedHogares();
        return this;
    }

    public abstract List<HogarDeTransito> refreshedHogares();
}
