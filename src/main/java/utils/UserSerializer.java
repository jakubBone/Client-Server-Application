package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class UserSerializer {
    private static final Logger logger = LogManager.getLogger(UserSerializer.class);

    public void writeToJson(User user, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
            logger.info("Successfully serialized user data for {} to file: {}", user.getUsername(), filePath);
        } catch(IOException ex) {
            logger.error("Error - failed to serialize data for {} to JSON at {}: ", user.getUsername(), filePath, ex);
        }
    }

    public User readFromJson(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            User user = gson.fromJson(reader, User.class);
            logger.info("Successfully deserialized user data from file: {}", filePath);
            return user;
        } catch(IOException ex) {
            logger.error("Error - failed to deserialize data from JSON at {}: ", filePath, ex);
            return null;
        }
    }

    /*public User readFromJson(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            User user = gson.fromJson(reader, User.class);
            logger.info("Successfully deserialized user data from file: {}", filePath);
            return gson.fromJson(reader, User.class);
        } catch(IOException ex) {
            logger.error("Error - failed to deserialize data to JSON  ", ex);
        }
        return null;
    }*/
}


