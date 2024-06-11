package user;

import lombok.Getter;
import lombok.Setter;
import shared.ResponseMessage;
import shared.JsonConverter;
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
    public static boolean ifAdminSwitched;

    public UserManager() {
        this.admin = new Admin();
        jsonConverter = new JsonConverter();
        jsonConverter.saveUserData(admin);
        usersList = new ArrayList<>();
        usersList.add(admin);
        log.info("UserManager instance created");
    }

     /*
      * Registers a new user with the specified username and password
      * If the username already exists, it's failure
      * Otherwise, successful registration
      */


    public String registerAndGetResponse(String username, String password) {
        log.info("Registration attempted for user: {}", username);

        if (isUserExistsInSystem(username)) {
            log.info("Registration attempt failed - user already exists: {}", username);
            return ResponseMessage.REGISTRATION_FAILED_USER_EXISTS.getResponse();
        }

        register(username, password);
        log.info("Registration successful for new user: {}", username);
        return ResponseMessage.REGISTRATION_SUCCESSFUL.getResponse();
    }

    public void register(String username, String password) throws IllegalArgumentException {
        User newUser = new User(username, password, User.Role.USER);
        jsonConverter.saveUserData(newUser);
        usersList.add(newUser);
        currentLoggedInUser = newUser;
        log.info("User registered: {}", username);
    }


    public String loginAndGetResponse(String username, String password) {
        log.info("Login attempted for user: {}", username);
        User user = getUserByUsername(username);

        if (user == null) {
            log.info("Login attempt failed - user does not exist: {}", username);
            return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
        }

        if (!ifPasswordCorrect(password, user)) {
            log.info("Incorrect password attempt for user: {}", user.getUsername());
            return ResponseMessage.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
        }

        log.info("User password correct: {}", user.getUsername());
        login(user);
        log.info("User login succeeded: {}", user.getUsername());

        if (ifCurrentUserAdmin()) {
            return ResponseMessage.ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return ResponseMessage.USER_LOGIN_SUCCEEDED.getResponse();
        }
    }

    public void login(User existingUser) {
        currentLoggedInUser = existingUser;
        log.info("User logged in: {}", existingUser.getUsername());
    }

    public boolean isUserExistsInSystem(String username) {
        return getUserByUsername(username) != null;
    }

    public boolean ifPasswordCorrect(String password, User existingUser) {
        return existingUser.checkPassword(password);
    }

    public String getLogoutResponse() {
        log.info("User logout requested");
        logoutCurrentUser();
        return ResponseMessage.LOGOUT_SUCCEEDED.getResponse();
    }

    public void logoutCurrentUser() {
        log.info("User logout succeeded: {}", currentLoggedInUser.getUsername());
        ifAdminSwitched = false;
        currentLoggedInUser = null;
    }

    // Finds a user by the username
    public User getUserByUsername(String username) {
        for (User user : usersList) {
            if (username.equals(user.getUsername())) {
                log.info("User found on the list: {}", username);
                return user;
            }
        }
        log.warn("User not found on the list: {}", username);
        return null;
    }

    public boolean ifCurrentUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }

}
