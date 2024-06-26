package user;

import java.util.List;

import database.DataBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import shared.ResponseMessage;

import static org.jooq.impl.DSL.*;

/*
 * The UserManager class manages user-related operations such as registration, login, and logout.
 * It maintains a list of users and tracks the currently logged-in user.
 */

@Log4j2
@Getter
@Setter
public class UserManager {
    public static List<User> usersList;
    public static User currentLoggedInUser;
    public static boolean ifAdminSwitched;
    public Admin admin;
    private final String USERS_TABLE = "users";
    public final DataBase DATABASE;
    public final DSLContext CREATE;


    public UserManager() {
        this.DATABASE = new DataBase();
        this.CREATE = DSL.using(DATABASE.getConnection());
        this.admin = new Admin(DATABASE, CREATE);
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
        addUserToDataBase(newUser);
        currentLoggedInUser = newUser;
        log.info("User registered: {}", username);
    }

    public void addUserToDataBase(User user)  {
        CREATE.insertInto(table("users"),
                        field("username"),
                        field("password"),
                        field("role"),
                        field("hashed_password"))
                .values(user.getUsername(),
                        user.getPassword(),
                        user.getRole().toString(),
                        user.getHashedPassword())
                .execute();
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
        return existingUser.checkPassword(password, CREATE);
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
        log.info("Searching for user in the database: {}", username);
        Record record = CREATE.selectFrom("users")
                            .where(DSL.field("username").eq(username))
                            .fetchOne();

        if(record != null){
            User user = new User(record.getValue("username", String.class),
                                 record.getValue("password", String.class),
                                 User.Role.valueOf(record.getValue("role", String.class).toUpperCase()));
            log.info("User found in database: {}", username);
            return user;
        } else {
            log.warn("User not found in database: {}", username);
            return null;
        }
    }

    public boolean ifCurrentUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }
}
