package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JsonConverter;

import java.util.ArrayList;
import java.util.List;

 /*
  * The UserManager class manages user-related operations, including registration, login, logout
  * It maintains a list of users and tracks the currently logged-in user
  */
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

     /*
      * Registers a new user with the specified username and password
      * If the username already exists, it's failure
      * Otherwise, successful registration
      */
    public String register(String typedUsername, String typedPassword) throws IllegalArgumentException {
        JsonConverter jsonConverter = new JsonConverter();
        boolean userExists = false;
        String registerStatus = null;

        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            registerStatus = "User exist";
            logger.info("Registration attempt failed - user already exists: {}", typedUsername);
        } else {
            registerStatus = "User does not exist";
            User newUser = new User(typedUsername, typedPassword, User.Role.USER);
            jsonConverter.writeUserToPath(newUser, "C:\\Users\\Jakub Bone\\Desktop\\Z2J\\projects\\Client-Server\\" + newUser.getUsername() + ".json");
            usersList.add(newUser);
            currentLoggedInUser = newUser;
            logger.info("New user registered: {}", typedUsername);
        }
        return registerStatus;
    }


    // Attempts to log in a user with the specified username and password
    public User login(String typedUsername, String typedPassword){
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                if (!typedPassword.equals(existingUser.getPassword())) {
                    logger.info("Incorrect password attempt for user: {}", typedUsername);
                } else {
                    logger.info("User logged in successfully: {}", typedUsername);
                    return existingUser;
                }
            } else{
                logger.info("Login attempt failed - username not found: {}", typedUsername);
            }
        }
        return null;
    }

    public void logoutCurrentUser() {
        logger.info("User logged out: {}", currentLoggedInUser.getUsername());
        currentLoggedInUser = null;
    }

    // Finds a recipient by the username
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

    // Finds a user by the username
    public User findUserByUsername(String username){
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
