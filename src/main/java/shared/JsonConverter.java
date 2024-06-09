package shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.log4j.Log4j2;
import user.User;

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
        log.info("Serializing user data for {} to file: {}", user.getUsername(), filePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
            log.info("Serialized user data for {} to file: {}", user.getUsername(), filePath);
        } catch(IOException ex) {
            log.error("Error - failed to serialize data for {} to JSON at {}: ", user.getUsername(), filePath, ex);
        }
    }


    // Converts the server response to JSON format on the Server side
    public String serializeMessage() {
        log.info("Serializing message");
        try {
            Gson gson = new Gson();
            return gson.toJson(this) + "\n<<END>>";
        } catch (Exception e) {
            throw new IllegalStateException("Error - failed to serialize JsonResponse to JSON", e);
        }
    }

    // Converts a JSON string to a JsonConverter object on the Client side
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

    public void saveUserData(User newUser){
        writeUserToPath(newUser, "C:\\Users\\Jakub Bone\\Desktop\\Z2J\\projects\\Client-Server\\" + newUser.getUsername() + ".json");
    }


    @Override
    public String toString() {
        return message;
    }
}