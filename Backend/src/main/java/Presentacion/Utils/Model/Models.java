package Presentacion.Utils.Model;

import Presentacion.Utils.Model.TypeAdapters.HibernateProxyTypeAdapter;
import Presentacion.Utils.Model.TypeAdapters.LocalDateTypeAdapter;
import Presentacion.Utils.Model.TypeAdapters.LocalDateTypeAdapter2;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import com.google.gson.*;


import java.time.LocalDate;
import java.util.Map;

public class Models {
    private static Gson gson;
    public static <T extends Object> Model fromMap(Map<String, T> map) {
        Model model = new Model();
        model.putAll(map);

        return model;
    }

    public static Model fromJson(String json) {
        try {
            return gson().fromJson(json, Model.class);

        } catch (JsonSyntaxException e) {
            throw new ConversionFallidaException(e);
        }
    }

    public static String toJson(Object object) {
        try {
            return gson().toJson(object).replace("\\\\\"", "\\\""); //reemplazo \\" por \"
        } catch (JsonSyntaxException e) {
            throw new ConversionFallidaException(e);
        }
    }

    protected static Gson gson(){
        if(gson==null){
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
            .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();
        }

        return gson;
    }
}
