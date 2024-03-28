package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);
    public static void main(String[] args) {
        User userToJSON = new User("pablo", "dasdas", User.Role.ADMIN);
        String file = userToJSON.getUsername() + "Data.json";
        JsonUtil.writeDataToJson(userToJSON, file);

        //User userFromJSON = JsonUtil.readDataToJson(file);
        User userFromJSON = JsonUtil.readDataToJson(file);
        System.out.println("Content: " + userFromJSON);
        if (userFromJSON != null) {
            System.out.println("Odczytano użytkownika: " + userFromJSON.getUsername());
        }
    }

    public static void writeDataToJson(User user, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
        } catch(IOException ex) {
            logger.error("Error - failed to serialize data to JSON  ", ex);
        }
    }

    public static User readDataToJson(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, User.class);
        } catch(IOException ex) {
            logger.error("Error - failed to deserialize data to JSON  ", ex);
        }
        return null;
    }
}


