package user.manager;

import database.DatabaseConnection;
import database.UserDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import shared.ResponseStatus;
import user.credential.Admin;
import user.credential.User;


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
    public static boolean ifAdminSwitchedAndAuthorized;
    public Admin admin;
    private DSLContext create;
    private UserDAO userDAO;
    private AuthManager authManager;

    public UserManager() {
        this.create = DSL.using(DatabaseConnection.getInstance().getConnection());
        this.userDAO = new UserDAO(create);
        this.admin = new Admin();
        this.authManager = new AuthManager();
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

    public void changePassword(User user, String newPassword) {
        log.info("Attempting to password change for user: {}", user.getUsername());

        user.setPassword(newPassword);

        log.info("Attempting to upload database: {}", user.getUsername());
        userDAO.updateUserInDB(user);
        log.info("Data base upload succeeded {}", user.getUsername());

        log.info("Password change succeeded for user: {}", user.getUsername());
    }

    public void removeUser(User user) {
        log.info("Attempting to remove user: {}", user.getUsername());

        userDAO.removeUserFromDB(user.getUsername());

        log.info("User removal succeeded: {}", user.getUsername());
    }

    public void switchUser(User user) {
        log.info("Attempting to switch to user: {}", user.getUsername());
            UserManager.currentLoggedInUser = user;

            if(isUserAdmin()){
                UserManager.ifAdminSwitchedAndAuthorized = true;
            } else {
                UserManager.ifAdminSwitched = true;
            }

        log.info("Switched to user: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role) {
        log.info("Attempting to role change for user: {}", user.getUsername());

        user.setRole(role);
        userDAO.changeUserRoleInDB(user, role);

        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }

    public String logoutAndGetResponse() {
        log.info("User logout requested");

        ifAdminSwitched = false;
        currentLoggedInUser = null;

        return ResponseStatus.LOGOUT_SUCCEEDED.getResponse();
    }

    public boolean isUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }

}