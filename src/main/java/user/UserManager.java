package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JsonConverter;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    public static List<User> usersList;
    public static User currentLoggedInUser;
    public Admin admin;

    public UserManager() {
        this.admin = new Admin();
        usersList = new ArrayList<>();
        usersList.add(admin);
    }

    public void register(String typedUsername, String typedPassword) throws IllegalArgumentException {
        JsonConverter jsonConverter = new JsonConverter();
        boolean userExists = false;
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            logger.info("Registration attempt failed - user already exists: {}", typedUsername);
        } else {
            User newUser = new User(typedUsername, typedPassword, User.Role.USER);
            jsonConverter.writeDataToPath(newUser, "C:\\Users\\Jakub Bone\\Desktop\\Z2J\\projects\\Client-Server\\" + newUser.getUsername() + ".json");
            usersList.add(newUser);
            currentLoggedInUser = newUser;
            logger.info("New user registered: {}", typedUsername);
        }
    }

    public User login(String typedUsername, String typedPassword){
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                if (!typedPassword.equals(existingUser.getPassword())) {
                    logger.info("Incorrect password attempt for user: {}", typedUsername);
                } else {
                    logger.info("User logged in successfully: {}", typedUsername);
                    return existingUser;
                }
            }
        }
        logger.info("Login attempt failed - username not found: {}", typedUsername);
        return null;
    }

    public void logoutCurrentUser() {
        logger.info("User logged out: {}", currentLoggedInUser.getUsername());
        currentLoggedInUser = null;
    }

    public User getRecipientByUsername(String username){
        for(User recipient: usersList){
            if(username.equals(recipient.getUsername())){
                logger.info("Recipient found: {}", username);
                return recipient;
            }
        }
        logger.info("Recipient not found: {}", username);
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
        if (searchedUser != null) {
            logger.info("User found on the list: {}", username);
        } else {
            logger.warn("User not found on the list: {}", username);
        }
        return searchedUser;
    }

    public boolean isAdmin(){
        logger.info("Admin checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser.role.equals(User.Role.ADMIN);
    }
}
