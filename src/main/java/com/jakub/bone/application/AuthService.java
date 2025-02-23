package com.jakub.bone.application;

import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.utils.ResponseStatus;
import com.jakub.bone.domain.User;

@Log4j2
public class AuthService {

    public String register(String username, String password, UserService userManager) {
        log.info("Registration attempted for user: {}", username);
        User user = userManager.getUserDAO().getUserFromDB(username);

        if (user != null) {
            log.info("Registration attempt failed - user already exists: {}", username);
            return ResponseStatus.REGISTRATION_FAILED_USER_EXISTS.getResponse();
        }

        handleRegister(username, password, userManager);
        log.info("Registration successful for new user: {}", username);

        return ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse();
    }

    public String login(String username, String password, UserService userService) {
        log.info("Login attempted for user: {}", username);
        User user = userService.getUserDAO().getUserFromDB(username);
        if (user == null) {
            log.info("Login attempt failed - user does not exist: {}", username);
            return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
        }

        if (!isPasswordCorrect(password, user, userService)) {
            log.info("Incorrect password attempt for user: {}", user.getUsername());
            return ResponseStatus.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
        }

        log.info("User password correct: {}", user.getUsername());


        log.info("User login succeeded: {}", user.getUsername());

        SessionManager.getInstance().setCurrentUser(user);

        if (SessionManager.getInstance().isAdmin()) {
            return ResponseStatus.ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse();
        }
    }

    public void handleRegister(String username, String password, UserService userManager) throws IllegalArgumentException {
        /*User newUser = new User(username, password, User.Role.USER);

        userManager.getUserDAO().addUserToDB(newUser);
        UserService.currentLoggedInUser = newUser;*/
        User newUser = new User(username, password, User.Role.USER);
        userManager.getUserDAO().addUserToDB(newUser);
    }

    public boolean isPasswordCorrect(String password, User user, UserService userManager) {
        return userManager.getUserDAO().checkPasswordInDB(password, user.getUsername());
    }

    public String logout() {
        SessionManager.getInstance().setCurrentUser(null);
        return ResponseStatus.LOGOUT_SUCCEEDED.getResponse();
    }

}
