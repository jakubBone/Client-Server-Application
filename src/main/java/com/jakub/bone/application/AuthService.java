package com.jakub.bone.application;

import com.jakub.bone.session.SessionManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.domain.User;

import static com.jakub.bone.utils.ResponseStatus.*;

@Log4j2
@Getter
public class AuthService {

    private final SessionManager sessionManager;

    public AuthService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public String register(String username, String password, UserService userManager) {
        User user = userManager.getUserRepository().findUserByUsername(username);

        if (user != null) {
            log.warn("Registration failed – user already exists: {}", username);
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
            log.warn("Login failed – user not found: {}", username);
            return FAILED_TO_FIND_USER.getResponse();
        }

        if (!isPasswordCorrect(password, user, userService)) {
            log.warn("Login failed – incorrect password for user: {}", user.getUsername());
            return LOGIN_FAILED_INCORRECT_PASSWORD.getResponse();
        }

        sessionManager.setCurrentUser(user);
        log.info("User logged in successfully: {}", user.getUsername());

        if (sessionManager.isAdmin()) {
            return ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return USER_LOGIN_SUCCEEDED.getResponse();
        }
    }

    public boolean isPasswordCorrect(String password, User user, UserService userManager) {
        return userManager.getUserRepository().verifyUserPassword(password, user.getUsername());
    }

    public String logout() {
        sessionManager.setCurrentUser(null);
        log.info("Logout successful");
        return LOGOUT_SUCCEEDED.getResponse();
    }

}
