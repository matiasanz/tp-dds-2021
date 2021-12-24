package Presentacion.Utils;

import Presentacion.Utils.Model.Model;
import Presentacion.Utils.Model.Models;
import Utils.Exceptions.Presentacion.ConversionFallidaException;
import spark.Request;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequestUtils {
    public static Model getSession(Request req){
        Map<String, Object> sesion = req.session().attributes().stream().collect(
            Collectors.toMap(
                Function.identity()
                , atr->req.session().attribute(atr)
            )
        );

        return Models.fromMap(sesion);
    }

    public static Model getParams(Request req){
        return Models.fromMap(
            req.params().entrySet().stream().collect(Collectors.toMap(
            p -> p.getKey().substring(1)
            , Map.Entry::getValue
            )
        ));
    }

    public static Model getBody(Request req){
        try{
            return Optional.ofNullable(req.body()).map(Models::fromJson).orElse(new Model());
        } catch (ConversionFallidaException e){
            throw ErrorHandler.haltWithMessage(403, "El body no se recibio en formato json");
        }
    }

    public static Model getQueryParams(Request req){
        return Models.fromMap(req.queryMap().toMap());
    }

    public static Model getCookies(Request req){
        return Models.fromMap(req.cookies());
    }
}
