package user;

import utils.JsonConverter;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

 /*
  * The UserManager class manages user-related operations, including registration, login, logout
  * It maintains a list of users and tracks the currently logged-in user
  */

@Log4j2
public class UserManager {
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
            log.info("Registration attempt failed - user already exists: {}", typedUsername);
        } else {
            registerStatus = "User does not exist";
            User newUser = new User(typedUsername, typedPassword, User.Role.USER);
            jsonConverter.writeUserToPath(newUser, "C:\\Users\\Jakub Bone\\Desktop\\Z2J\\projects\\Client-Server\\" + newUser.getUsername() + ".json");
            usersList.add(newUser);
            currentLoggedInUser = newUser;
            log.info("New user registered: {}", typedUsername);
        }
        return registerStatus;
    }


    // Attempts to log in a user with the specified username and password
    public User login(String typedUsername, String typedPassword){
        for (User existingUser : usersList) {
            if (typedUsername.equals(existingUser.getUsername())) {
                if (!typedPassword.equals(existingUser.getPassword())) {
                    log.info("Incorrect password attempt for user: {}", typedUsername);
                } else {
                    log.info("User logged in successfully: {}", typedUsername);
                    return existingUser;
                }
            } else{
                log.info("Login attempt failed - username not found: {}", typedUsername);
            }
        }
        return null;
    }

    public void logoutCurrentUser() {
        log.info("User successfully logged out: {}", currentLoggedInUser.getUsername());
        currentLoggedInUser = null;
    }

    // Finds a recipient by the username
    public User getRecipientByUsername(String username){
        for(User recipient: usersList){
            if(username.equals(recipient.getUsername())){
                log.info("Recipient found: {}", username);
                return recipient;
            }
        }
        log.info("Recipient not found: {}", username);
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
            log.info("User found on the list: {}", username);
        } else {
            log.warn("User not found on the list: {}", username);
        }
        return searchedUser;
    }

    public boolean isAdmin(){
        log.info("Admin checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser.role.equals(User.Role.ADMIN);
    }
}
