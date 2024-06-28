package user;

import database.DataBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import shared.ResponseMessage;

import static org.jooq.impl.DSL.*;

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
    public final DataBase DATABASE;
    public final DSLContext CREATE;
    private final String USERS_TABLE = "users";

    public UserManager() {
        this.DATABASE = new DataBase();
        this.CREATE = DSL.using(DATABASE.getConnection());
        this.admin = new Admin(DATABASE, CREATE);
        log.info("UserManager instance created");
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
        addUserToDataBase(newUser);
        currentLoggedInUser = newUser;
        log.info("User registered: {}", username);
    }

    public void addUserToDataBase(User user)  {
        CREATE.insertInto(table(USERS_TABLE),
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

    /**
     * Retrieves a user from the database by username.
     * @return The User object, or null if not found
     */
    public User getUserByUsername(String username) {
        log.info("Searching for user in the database: {}", username);

        Record record = CREATE.selectFrom(USERS_TABLE)
                .where(DSL.field("username").eq(username))
                .fetchOne();

        if (record == null) {
            log.warn("User not found in database: {}", username);
            return null;
        }

        User user = new User(
                record.getValue("username", String.class),
                record.getValue("password", String.class),
                User.Role.valueOf(record.getValue("role", String.class).toUpperCase())
        );

        log.info("User found in database: {}", username);
        return user;
    }

    public boolean isUserAdmin(){
        log.info("Admin role checking for user: {}", currentLoggedInUser.getUsername());
        return currentLoggedInUser != null && currentLoggedInUser.getRole().equals(User.Role.ADMIN);
    }

    public void close() {
        if (DATABASE != null) {
            DATABASE.disconnect();
        }

    }
}