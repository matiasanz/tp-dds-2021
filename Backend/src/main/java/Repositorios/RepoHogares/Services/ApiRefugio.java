package Repositorios.RepoHogares.Services;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiRefugio {

    String urlApi = "https://api.refugiosdds.com.ar/api/";

    @Headers({"Authorization: Bearer yhM9rNJLKoG6cTEuAeuaWEKL1haGdzXFaVmXwvAN5r8ueUyOWZ7tIRgBmtWN"})
    @GET("hogares")
    Call<ResponseHogares> getHogares(@Query("offset") int pagina);

    static ApiRefugio create(){
        return new Retrofit.Builder()
            .baseUrl(urlApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRefugio.class);
    }
}
