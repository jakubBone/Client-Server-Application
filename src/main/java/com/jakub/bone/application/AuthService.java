package com.jakub.bone.application;

import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.domain.User;

import static com.jakub.bone.utils.ResponseStatus.*;

@Log4j2
public class AuthService {

    public String register(String username, String password, UserService userManager) {
        User user = userManager.getUserRepository().findUserByUsername(username);

        if (user != null) {
            log.info("Registration attempt failed - user already exists: {}", username);
            return REGISTRATION_FAILED_USER_EXISTS.getResponse();
        }

        User newUser = new User(username, password, User.Role.USER);
        userManager.getUserRepository().createUser(newUser);
        log.info("Registration successful for new user: {}", username);

        return REGISTRATION_SUCCESSFUL.getResponse();
    }

    public String login(String username, String password, UserService userService) {
        User user = userService.getUserRepository().findUserByUsername(username);
        if (user == null) {
            log.info("Login attempt failed - user does not exist: {}", username);
            return FAILED_TO_FIND_USER.getResponse();
        }

        if (!isPasswordCorrect(password, user, userService)) {
            log.info("Incorrect password attempt for user: {}", user.getUsername());
            return LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
        }

        SessionManager.getInstance().setCurrentUser(user);
        log.info("Login success for user: {}", user.getUsername());

        if (SessionManager.getInstance().isAdmin()) {
            return ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return USER_LOGIN_SUCCEEDED.getResponse();
        }
    }

    public boolean isPasswordCorrect(String password, User user, UserService userManager) {
        return userManager.getUserRepository().verifyUserPassword(password, user.getUsername());
    }

    public String logout() {
        SessionManager.getInstance().setCurrentUser(null);
        log.info("Logout successful");

        return LOGOUT_SUCCEEDED.getResponse();
    }

}
