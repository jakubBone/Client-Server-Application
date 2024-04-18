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

    public void writeDataToJson(User user, String filePath){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(user, writer);
            writer.flush();
        } catch(IOException ex) {
            logger.error("Error - failed to serialize data to JSON  ", ex);
        }
    }

    public User readDataToJson(String filePath){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, User.class);
        } catch(IOException ex) {
            logger.error("Error - failed to deserialize data to JSON  ", ex);
        }
        return null;
    }
}


