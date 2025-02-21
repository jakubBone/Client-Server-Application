package service;

import database.DataSource;
import repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import utils.ResponseStatus;
import domain.Admin;
import domain.User;

@Log4j2
@Getter
@Setter
public class UserService {
    public static User currentLoggedInUser;
    public static boolean ifSwitchedToNonAdminUser;
    public static boolean ifSwitchedToAdminUser;
    public Admin admin;
    private DSLContext create;
    private UserRepository userDAO;
    private AuthService authManager;

    public UserService() {
        this.create = DSL.using(DataSource.getInstance().getConnection());
        this.userDAO = new UserRepository(create);
        this.admin = new Admin();
        this.authManager = new AuthService();
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
            UserService.currentLoggedInUser = user;

            if(isUserAdmin()){
                UserService.ifSwitchedToAdminUser = true;
            } else {
                UserService.ifSwitchedToNonAdminUser = true;
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

        ifSwitchedToNonAdminUser = false;
        currentLoggedInUser = null;

        return ResponseStatus.LOGOUT_SUCCEEDED.getResponse();
    }


    public boolean isLoggedIn() {
        return currentLoggedInUser != null;
    }

    public boolean isUserAdmin() {
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return isLoggedIn() && currentLoggedInUser.getRole() == User.Role.ADMIN;
    }
}