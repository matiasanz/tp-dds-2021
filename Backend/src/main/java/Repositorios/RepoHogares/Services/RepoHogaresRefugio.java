package Repositorios.RepoHogares.Services;

import Modelo.Hogares.HogarDeTransito;
import Repositorios.RepoHogares.RepoHogares;
import Utils.Exceptions.RespuestaHogaresNoOkException;
import Utils.Exceptions.TiempoDeEsperaAgotadoException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepoHogaresRefugio extends RepoHogares {

    private ApiRefugio refService = ApiRefugio.create();

    //Cachea la lista de hogares
    @Override
    public List<HogarDeTransito> refreshedHogares(){
        List<HogarDeTransito> hogares = new ArrayList<>();

        for(int pagina=1 ;; pagina++) {
            Response<ResponseHogares> response = getHogaresDePagina(pagina);

            switch (response.code()){
                case 200: {
                    hogares.addAll(response.body().getHogares());
                    break; //Continua el for
                }
                case 400: {
                    return hogares;
                }
                default:
                    throw new RespuestaHogaresNoOkException(response.code());
            }
        }
    }

    private Response<ResponseHogares> getHogaresDePagina(int pagina) {
        Call<ResponseHogares> requestHogares = refService.getHogares(pagina);

        try {
            return requestHogares.execute();
        } catch (IOException e) {
            throw new TiempoDeEsperaAgotadoException();
        }
    }
}
