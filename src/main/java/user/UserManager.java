package user;

import exceptions.UserAuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private List<User> usersList;
    private List <String> passwordChangeRequesters;
    private List <String> removeAccountRequesters;
    public static User currentLoggedInUser;

    public UserManager() {
        this.usersList = new ArrayList<>();
        this.passwordChangeRequesters = new ArrayList<>();
        this.removeAccountRequesters = new ArrayList<>();
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }

    public void register(String typedUserName, String typedPassword) throws IllegalArgumentException {
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

    public User login(String typedUserName, String typedPassword) /*throws UserAuthenticationException*/ {
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                if (!ifPasswordEqual(typedPassword, user.getHashedPassword())) {
                    logger.info("Incorrect password");
                    //throw new UserAuthenticationException("Incorrect password");
                } else {
                    user.isUserLoggedIn = true;
                    logger.info("Login successful");
                    return user;
                }
            }
        }
        return null;
        //throw new UserAuthenticationException("Login failed: user not found");
    }

    public void logout(String typedUserName) throws UserAuthenticationException {
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

    public void requestAccountRemovalByAdmin(String typedUserName) throws UserAuthenticationException {
        User userToDelete = null;
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            usersList.remove(userToDelete);
            logger.info("Account delete successful");
        } else {
            throw new UserAuthenticationException("User not found");
        }
    }

    public void requestPasswordChangeByAdmin(String typedUserName, String newPassword, String oldPassword) throws UserAuthenticationException {
        for (User user : usersList) {
            if (isUserNameEqual(user, typedUserName)) {
                if (ifPasswordEqual(oldPassword, user.getHashedPassword())) {
                    user.setPassword(newPassword);
                    user.setHashedPassword(newPassword.hashCode());
                    logger.info("Password change successful");
                    return;
                } else {
                    throw new UserAuthenticationException("Incorrect old password");
                }
            }
        }
        throw new UserAuthenticationException("User not found");
    }

    public User getRecipientByUsername(String username){
        for(User recipient: usersList){
            if(username.equals(recipient.getUsername())){
                return recipient;
            }
        }
        return null;
    }

    public boolean isUserNameEqual(User user, String userName) {
        return user.getUsername().equals(userName);
    }

    public boolean ifPasswordEqual(String typedPassword, int hashedPassword) {
        return typedPassword.hashCode() == hashedPassword;
    }
}
