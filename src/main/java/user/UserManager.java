package user;

import database.DatabaseConnection;
import database.UserDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import shared.ResponseMessage;


/**
 * The UserManager class manages user accounts and operations such as registration, login, and role changes.
 * It interacts with the database to store and retrieve user information.
 */

@Log4j2
@Getter
@Setter
public class UserManager {
    public static User currentLoggedInUser;
    public static boolean ifAdminSwitched;
    public Admin admin;
    private final DSLContext create;
    private final UserDAO userDAO;

    public UserManager() {
        this.create = DSL.using(DatabaseConnection.getInstance().getConnection());
        this.userDAO = new UserDAO(create);
        this.admin = new Admin();
    }

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
        userDAO.addUser(newUser);
        currentLoggedInUser = newUser;
        log.info("User registered: {}", username);
    }

    public String loginAndGetResponse(String username, String password) {
        log.info("Login attempted for user: {}", username);
        User user = userDAO.getUserFromDB(username);

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

        if (isUserAdmin()) {
            return ResponseMessage.ADMIN_LOGIN_SUCCEEDED.getResponse();
        } else {
            return ResponseMessage.USER_LOGIN_SUCCEEDED.getResponse();
        }
    }

    public void login(User existingUser) {
        currentLoggedInUser = existingUser;
        log.info("User logged in: {}", existingUser.getUsername());
    }

    public User getUserByUsername(String username) {
        log.info("Searching for user in the database: {}", username);

        User user = userDAO.getUserFromDB(username);

        if (user == null) {
            log.warn("User not found in database: {}", username);
            return null;
        }

        log.info("User found in database: {}", username);
        return user;
    }

    public boolean checkPassword(String typedPassword, User user) {
        log.info("Checking password for user: {}", user.getUsername());

        return userDAO.checkPasswordInDB(typedPassword, user.getUsername());
    }

    public void changePassword(User user, String newPassword) {
        log.info("Attempting to password change for user: {}", user.getUsername());

        user.setPassword(newPassword);
        userDAO.updateUserInDB(user);

        log.info("Password change succeeded for user: {}", user.getUsername());
    }

    public void deleteUser(User user) {
        log.info("Attempting to delete user: {}", user.getUsername());

        userDAO.deleteUser(user.getUsername());

        log.info("User deletion succeeded: {}", user.getUsername());
    }

    public void switchUser(User user) {
        log.info("Attempting to switch to user: {}", user.getUsername());

            UserManager.currentLoggedInUser = user;
            UserManager.ifAdminSwitched = true;

        log.info("Switched to user: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role) {
        log.info("Attempting to role change for user: {}", user.getUsername());

        userDAO.changeUserRole(user, role);

        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }

    public boolean isUserExistsInSystem(String username) {
        return userDAO.getUserFromDB(username) != null;
    }

    public boolean ifPasswordCorrect(String password, User user) {
        return checkPassword(password, user);
    }

    public String logoutAndGetResponse() {
        log.info("User logout requested");

        ifAdminSwitched = false;
        currentLoggedInUser = null;

        log.info("User logout succeeded: {}", currentLoggedInUser.getUsername());
        return ResponseMessage.LOGOUT_SUCCEEDED.getResponse();
    }

    public boolean isUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());

        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }
}