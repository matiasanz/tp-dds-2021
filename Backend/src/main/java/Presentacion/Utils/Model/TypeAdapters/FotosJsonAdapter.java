package Presentacion.Utils.Model.TypeAdapters;

import Modelo.Imagenes.Imagen;
import Presentacion.Utils.Imagenes.ImagenDTO;
import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class FotosJsonAdapter implements JsonSerializer<List<String>>, JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new LinkedList<>();
    }

    @Override
    public JsonElement serialize(List<String> element, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        for(String s: element){
            ImagenDTO imagen = new ImagenDTO(s);

            array.add(
                String.join(","
                    , "data: image/"+ imagen.getExtension() + ";base64"
                    , imagen.getContent()
                )
            );
        }

        return array;
    }
}