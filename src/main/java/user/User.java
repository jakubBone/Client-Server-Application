package user;

import exceptions.UserAuthenticationException;
import mail.MailBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    protected static final Logger logger = LogManager.getLogger(User.class);
    protected String username;
    protected String password;
    protected int hashedPassword;
    protected Role role;
    protected MailBox mailBox;
    protected boolean isUserLoggedIn;

    public enum Role {
        ADMIN,
        USER;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mailBox = new MailBox();
        this.isUserLoggedIn = false;
        this.hashedPassword = password.hashCode();
    }

    public void register(String typedUserName, String typedPassword, List<User> usersList) throws IllegalArgumentException {
        boolean userExists = false;
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            throw new IllegalArgumentException("User already exists");
        } else {
            usersList.add(new User(typedUserName, typedPassword, User.Role.USER));
            logger.info("Registration successful");
        }
    }

    public void login(String typedUserName, String typedPassword, List<User> usersList) throws UserAuthenticationException {
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                if (!ifPasswordEqual(typedPassword, user.getHashedPassword())) {
                    logger.info("Incorrect password");
                    throw new UserAuthenticationException("Incorrect password");
                } else {
                    user.isUserLoggedIn = true;
                    logger.info("Login successful");
                    return;
                }
            }
        }
        throw new UserAuthenticationException("Login failed: user not found");
    }

    public void logout(String typedUserName, List<User> usersList) throws UserAuthenticationException {
        boolean isUserFound = false;
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                isUserFound = true;
                if (!user.isUserLoggedIn) {
                    throw new UserAuthenticationException("User is already logged out.");
                } else {
                    user.isUserLoggedIn = false;
                    logger.info("Logout successful for user: " + user.getUsername());
                    return;
                }
            }
        }
        if (!isUserFound) {
            throw new UserAuthenticationException("User not found in the list.");
        }
    }

    public void requestPasswordChange(List <String> passwordChangeRequests)  {
        passwordChangeRequests.add(this.username);
    }

    public void requestAccountRemoval(List <String> accountRemovalRequests) {
        accountRemovalRequests.add(this.username);
    }

    public boolean isUserNameEqual(User user, String userName) {
        return user.getUsername().equals(userName);
    }

    public boolean ifPasswordEqual(String typedPassword, int hashedPassword) {
        return typedPassword.hashCode() == hashedPassword;
    }
}

