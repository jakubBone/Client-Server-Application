package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.UserSerializer;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    public static List<User> usersList;
    public static User currentLoggedInUser;
    public Admin admin;

    public UserManager() {
        usersList = new ArrayList<>();
        this.admin = new Admin();
        usersList.add(admin);
    }

    public void register(String typedUsername, String typedPassword) throws IllegalArgumentException {
        UserSerializer userSerializer = new UserSerializer();
        boolean userExists = false;
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            logger.info("User already exists");
        } else {
            User newUser = new User(typedUsername, typedPassword, User.Role.USER);
            userSerializer.writeDataToJson(newUser, "C:\\Users\\Jakub Bone\\Desktop\\Z2J\\projects\\Client-Server\\" + newUser.getUsername() + ".json");
            usersList.add(newUser);
            currentLoggedInUser = newUser;
            logger.info("Registration successful");
        }
    }

    public User login(String typedUsername, String typedPassword){
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                if (!typedPassword.equals(existingUser.getPassword())) {
                    logger.info("Incorrect password");
                } else {
                    logger.info("Login successful");
                    return existingUser;
                }
            }
        }
        return null;
    }

    public void logoutCurrentUser() {
        currentLoggedInUser = null;
    }

    public User getRecipientByUsername(String username){
        for(User recipient: usersList){
            if(username.equals(recipient.getUsername())){
                return recipient;
            }
        }
        return null;
    }

    public User findUserOnTheList(String username){
        User searchedUser = null;
        for (User user : usersList) {
            if (username.equals(user.getUsername())) {
                searchedUser = user;
                break;
            }
        }
        return searchedUser;
    }

    public boolean isAdmin(){
        return currentLoggedInUser.role.equals(User.Role.ADMIN);
    }
}
