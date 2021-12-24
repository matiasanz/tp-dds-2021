package Presentacion.Utils.Model.TypeAdapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateTypeAdapter implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return json.isJsonPrimitive()? LocalDate.parse(json.getAsJsonPrimitive().getAsString())
            : new Gson().fromJson(json, LocalDate.class);

    }
}
