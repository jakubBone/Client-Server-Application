package user.manager;

import database.UserDAO;
import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.credential.User;

@Log4j2
public class AuthManager {
    public String registerUser(String username, String password, UserDAO userDAO) {
        log.info("Registration attempted for user: {}", username);
        User user = userDAO.getUserFromDB(username);

        if (user != null) {
            log.info("Registration attempt failed - user already exists: {}", username);
            return ResponseMessage.REGISTRATION_FAILED_USER_EXISTS.getResponse();
        }

        handleRegister(username, password, userDAO);
        log.info("Registration successful for new user: {}", username);
        return ResponseMessage.REGISTRATION_SUCCESSFUL.getResponse();
    }

    public void handleRegister(String username, String password, UserDAO userDAO) throws IllegalArgumentException {
        User newUser = new User(username, password, User.Role.USER);

        userDAO.addUserToDB(newUser);
        UserManager.currentLoggedInUser = newUser;

        log.info("User registered: {}", username);
    }

    public String loginUser(String username, String password, UserDAO userDAO) {
        log.info("Login attempted for user: {}", username);
        User user = userDAO.getUserFromDB(username);

        if (user == null) {
            log.info("Login attempt failed - user does not exist: {}", username);
            return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
        }

        if (!ifPasswordCorrect(password, user, userDAO)) {
            log.info("Incorrect password attempt for user: {}", user.getUsername());
            return ResponseMessage.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
        }

        log.info("User password correct: {}", user.getUsername());

        handleLogin(user);

        log.info("User login succeeded: {}", user.getUsername());

        if (isUserAdmin()) {
            return ResponseMessage.ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return ResponseMessage.USER_LOGIN_SUCCEEDED.getResponse();
        }


    }
    public void handleLogin(User existingUser) {
        UserManager.currentLoggedInUser = existingUser;
    }

    public boolean checkPassword(String typedPassword, User user, UserDAO userDAO) {
        log.info("Checking password for user: {}", user.getUsername());

        return userDAO.checkPasswordInDB(typedPassword, user.getUsername());
    }

    public boolean ifPasswordCorrect(String password, User user, UserDAO userDAO) {
        return checkPassword(password, user, userDAO);
    }

    public boolean isUserAdmin(){
        log.info("Admin role checking for user: {}", UserManager.currentLoggedInUser.getUsername());
        return UserManager.currentLoggedInUser != null && UserManager.currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }
}
