package com.jakub.bone.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JsonConverter {
    private static Gson gson;

    public JsonConverter() {
        this.gson = new Gson();
    }

    public static String serialize(Object obj) {
        try {
            gson = new Gson();
            return gson.toJson(obj) + "\n<<END>>";
        } catch (Exception e) {
            throw new IllegalStateException("Error - failed to serialize JsonResponse to JSON", e);
        }
    }

    /*public static String deserialize(String json) {
        try {
            JsonConverter jsonConverter = gson.fromJson(json, JsonConverter.class);
            log.info("Deserialized message: {}", json);
            return jsonConverter.toString();
        } catch (JsonSyntaxException e) {
            log.error("Deserialization error: {}", e.getMessage());
            throw new IllegalArgumentException("Błędny format JSON", e);
        }
    }*/

    public static <T> T deserialize(String json, Class<T> classOfT) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            log.error("Deserialization error: {}", e.getMessage());
            throw new IllegalArgumentException("Błędny format JSON", e);
        }
    }

   /* @Override
    public String toString() {
        return message;
    }*/

}