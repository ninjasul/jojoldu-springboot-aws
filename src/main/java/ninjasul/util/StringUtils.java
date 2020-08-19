package ninjasul.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StringUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> String toPrettyJson(T json) {
        if (json instanceof String) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(String.valueOf(json)).getAsJsonObject();
            String prettyJson = gson.toJson(jsonObj);
            return prettyJson;
        }
        else {
            return gson.toJson(json);
        }
    }
}
