package Presentacion.Utils.Model;

import Utils.Exceptions.Presentacion.ConversionFallidaException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Model extends HashMap<String, Object> {
    public Model(){}

    public Model(String key, Object value){
        append(key, value);
    }

    public Model append(String key, Object value){
        put(key, value);
        return this;
    }

    public String toJson(){
        return Models.toJson(this);
    }

    public String stringValue(String key){
        return typeValue(key, Object::toString);
    }

    public Long longValue(String key) {
        Object value = get(key);

        if (value instanceof Double) {
            return typeValue(key, v -> new Double((double) v).longValue());
        } else if(value instanceof String){
            return typeValue(key, v -> new Double((String) v).longValue());
        }else{
            return typeValue(key, v-> (long) v);
        }
    }

    public Integer intValue(String key){
        Object o = get(key);
        return
            o instanceof Double? typeValue(key, v-> (new Double((double) v)).intValue())
                : o instanceof String? typeValue(key, v->(new Double((String) v).intValue()))
                : o instanceof Integer? typeValue(key, v->((Integer) v))
                : null;
    }

    public <E extends Enum<E>> E getEnum( String text, Class<E> klass){
        return typeValue(text, o -> Enum.valueOf(klass, (String) o ));
    }

    public <T> List<T> listValue(Class<T> clase){
        String key = "@k";
        return new Model(key, this).listValue(key, clase);
    }

    public <T> List<T> listValue(String key, Class<T> clase) {
        return typeValue(key, value ->
            ((List<Map<String, Object>>) value)
                .stream()
                .map(Models::fromMap)
                .map(m -> m.classValue(clase))
                .collect(Collectors.toList()));
    }

    public <T> T classValue(String key, Class<T> clase){
        Model modelo = subModel(key);
        return modelo==null? null: modelo.classValue(clase);
    }

    public <T> T classValue(Class<T> clase){
        try{
            return Models.gson().fromJson(toJson(), clase);
        } catch (JsonSyntaxException e){
            throw new ConversionFallidaException(e);
        }
    }

    public Model subModel(String key){
        try {
            return typeValue(key, val->Models.fromMap((Map<String, Object>)val));
        } catch (Exception e) {
            throw new ConversionFallidaException(e, key);
        }
    }

    private <T> T typeValue(String key, Function<Object, T> parser){
        Object value = get(key);
        try{
            return parser.apply(value);

        } catch (NullPointerException e){
            return null; //TODO: Ver si hace falta validar

        } catch (Exception e){ //TODO: Fuerte
            System.out.println(e.getMessage());
            throw new ConversionFallidaException(key, value);
        }
    }

    public <T> List<T> primitiveList(String key) {
        return typeValue(key, v -> (List<T>) v);
    }

    public Boolean getBoolean(String aprobada) {
        return typeValue(aprobada, o -> (Boolean) o);
    }
}