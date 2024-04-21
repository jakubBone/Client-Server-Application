package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConverter {
    private static final Logger logger = LogManager.getLogger(JsonConverter.class);
    private String message;

    public JsonConverter(String message) {
        this.message = message;
    }
    public JsonConverter() {

    }

    public void writeUserToPath(User user, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
            logger.info("Successfully serialized user data for {} to file: {}", user.getUsername(), filePath);
        } catch(IOException ex) {
            logger.error("Error - failed to serialize data for {} to JSON at {}: ", user.getUsername(), filePath, ex);
        }
    }


    public User readUserFromPath(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            User user = gson.fromJson(reader, User.class);
            logger.info("Successfully deserialized user data from file: {}", filePath);
            return gson.fromJson(reader, User.class);
        } catch(IOException ex) {
            logger.error("Error - failed to deserialize data to JSON  ", ex);
        }
        return null;
    }

    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this) + "\n<<END>>";
        } catch (Exception e) {
            throw new IllegalStateException("Error - failed to serialize JsonResponse to JSON", e);
        }
    }


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