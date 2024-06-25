package shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import user.User;

/*
  * The JsonConverter class provides utilities for converting objects to and from JSON
  * It includes methods for serializing and deserializing user data
  */

@Log4j2
public class JsonConverter {
    private String message;

    public JsonConverter(String message) {
        this.message = message;
    }

    public String serializeMessage() {
        log.info("Serializing message");
        try {
            Gson gson = new Gson();
            return gson.toJson(this) + "\n<<END>>";
        } catch (Exception e) {
            throw new IllegalStateException("Error - failed to serialize JsonResponse to JSON", e);
        }
    }

    public static String deserializeMessage(String json) {
        log.info("Deserializing message");
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("Input JSON is null or empty");
        }
        try {
            Gson gson = new Gson();
            JsonConverter jsonConverter = gson.fromJson(json, JsonConverter.class);
            log.info("Deserialized message: {}", json);
            return jsonConverter.toString();
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Error deserializing JSON. Please check syntax", e);
        }
    }

    @Override
    public String toString() {
        return message;
    }
}