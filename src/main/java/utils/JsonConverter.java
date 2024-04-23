package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.log4j.Log4j2;
import user.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
    public JsonConverter() {

    }

    // Writes a user object to the specified file path in JSON format
    public void writeUserToPath(User user, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
            log.info("Successfully serialized user data for {} to file: {}", user.getUsername(), filePath);
        } catch(IOException ex) {
            log.error("Error - failed to serialize data for {} to JSON at {}: ", user.getUsername(), filePath, ex);
        }
    }

    /*
     * Reads a User object from the specified file path in JSON format
     * Needs to be improved and implement in appropriate place
     */
    public User readUserFromPath(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            User user = gson.fromJson(reader, User.class);
            log.info("Successfully deserialized user data from file: {}", filePath);
            return gson.fromJson(reader, User.class);
        } catch(IOException ex) {
            log.error("Error - failed to deserialize data to JSON  ", ex);
        }
        return null;
    }


    // Converts the server response to JSON format on the Server side
    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this) + "\n<<END>>";
        } catch (Exception e) {
            throw new IllegalStateException("Error - failed to serialize JsonResponse to JSON", e);
        }
    }

    // Converts a JSON string to a JsonConverter object on the Client side
    public static String fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("Input JSON is null or empty.");
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, JsonConverter.class).toString();
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Error deserializing JSON. Please check syntax.", e);
        }
    }

    @Override
    public String toString() {
        return message;
    }
}