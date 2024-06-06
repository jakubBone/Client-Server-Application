package user;

import lombok.Getter;
import lombok.Setter;
import operations.OperationResponses;
import utils.JsonConverter;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

 /*
  * The UserManager class manages user-related operations, including registration, login, logout
  * It maintains a list of users and tracks the currently logged-in user
  */

@Log4j2
@Getter
@Setter
public class UserManager {
    public static List<User> usersList;
    public static User currentLoggedInUser;
    public Admin admin;
    JsonConverter jsonConverter;

    public boolean ifAdminSwitched;

    public UserManager() {
        this.admin = new Admin();
        jsonConverter = new JsonConverter();
        jsonConverter.saveUserData(admin);
        usersList = new ArrayList<>();
        usersList.add(admin);
    }

     /*
      * Registers a new user with the specified username and password
      * If the username already exists, it's failure
      * Otherwise, successful registration
      */


     public String getRegisterResponse(String typedUsername, String typedPassword){
         log.info("Registration attempted for user: {}", typedPassword);
         if(isUserExists(typedUsername)){
             log.info("Registration attempt failed - user already exists: {}", typedUsername);
             return OperationResponses.REGISTRATION_FAILED_USER_EXISTS.getResponse();
         } else {
             register(typedUsername, typedPassword);
             log.info("Registration successful for new user: {}", typedUsername);
             return OperationResponses.REGISTRATION_SUCCESSFUL.getResponse();
         }
     }

    public void register(String typedUsername, String typedPassword) throws IllegalArgumentException {
        User newUser = new User(typedUsername, typedPassword, User.Role.USER);
        jsonConverter.saveUserData(newUser);
        usersList.add(newUser);
        currentLoggedInUser = newUser;
    }

    ///////////////////////////////////////////////////////////////////////
    public String getLoginResponse(String typedUsername, String typedPassword) {
        if (isUserExists(typedUsername)) {
            User existingUser = getExistingUser(typedUsername);
            if(ifPasswordCorrect(typedPassword, existingUser)){
                log.info("User password correct: {}", existingUser.getUsername());
                login(existingUser);
                log.info("User logged in successfully: {}", existingUser.getUsername());
                    if(ifCurrentUserAdmin()){
                        return OperationResponses.LOGIN_SUCCESSFUL_ADMIN.getResponse();
                    } else {
                        return OperationResponses.LOGIN_SUCCESSFUL_USER.getResponse();
                    }

            } else {
                log.info("Incorrect password attempt for user: {}", existingUser.getUsername());
                return OperationResponses.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
            }
        } else {
            log.info("Login attempt failed - user does not exist: {}", typedUsername);
            return OperationResponses.LOGIN_FAILED_USER_NOT_FOUND.getResponse();
        }
    }
    public void login(User existingUser) {
        currentLoggedInUser = existingUser;
    }

    public User getExistingUser(String username) {
        User existingUser = getUserByUsername(username);
        if (existingUser == null) {
            return null;
        } else {
            return existingUser;
        }
    }

    public boolean isUserExists(String username) {
        User existingUser = getUserByUsername(username);
        if (existingUser == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean ifPasswordCorrect(String typedPassword, User existingUser) {
        if (existingUser.checkPassword(typedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public String getLogoutResponse() {
        logoutCurrentUser();
        return OperationResponses.SUCCESSFULLY_LOGGED_OUT.getResponse();
    }

    public void logoutCurrentUser() {
        log.info("User successfully logged out: {}", currentLoggedInUser.getUsername());
        ifAdminSwitched = false;
        currentLoggedInUser = null;
    }

    // Finds a user by the username
    public User getUserByUsername(String username){
        for (User user : usersList) {
            if (username.equals(user.getUsername())) {
                log.info("User found on the list: {}", username);
                return user;
            }
        }
        log.warn("User not found on the list: {}", username);
        return null;
    }


    public void switchUser(String username) {
            User user = getUserByUsername(username);
            if (user != null) {
                currentLoggedInUser = user;
                ifAdminSwitched = true;
                log.info("Admin switched to user: {}", username);
            } else {
                log.info("Admin failed to switch to user: {}", username);
            }
    }

    public boolean ifCurrentUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }

}
