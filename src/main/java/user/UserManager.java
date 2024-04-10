package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    public static List<User> usersList;
    private List <String> passwordChangeRequesters;
    private List <String> removeAccountRequesters;
    public static User currentLoggedInUser;
    public Admin admin;

    public UserManager() {
        usersList = new ArrayList<>();
        this.admin = new Admin();
        usersList.add(admin);
        this.passwordChangeRequesters = new ArrayList<>();
        this.removeAccountRequesters = new ArrayList<>();
    }

    public void register(String typedUserName, String typedPassword) throws IllegalArgumentException {
        logger.info("in user manager register");
        boolean userExists = false;
        for (User existingUser : usersList) {
            if (isUserIdEqual(existingUser, typedUserName)) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            logger.info("User already exists");
        } else {
            usersList.add(new User(typedUserName, typedPassword, User.Role.USER));
            logger.info("Registration successful");
        }
    }

    public User login(String typedUsername, String typedPassword){
        for (User existingUser : usersList) {
            if (isUserIdEqual(existingUser, typedUsername)) {
                if (!ifPasswordEqual(existingUser.getHashedPassword(), typedPassword)) {
                    logger.info("Incorrect password");
                } else {
                    existingUser.isUserLoggedIn = true;
                    logger.info("Login successful");
                    return existingUser;
                }
            }
        }
        return null;
    }

    /*public void logout(String typedUserName) throws UserAuthenticationException {
        boolean isUserFound = false;
        for (User user : usersList) {
            if (isUserIDEqual(user, typedUserName)) {
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
    }*/

    /*public void requestAccountRemovalByAdmin(String typedUserName) throws UserAuthenticationException {
        User userToDelete = null;
        for (User user : usersList) {
            if (isUserIDEqual(user, typedUserName)) {
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
    }*/

    /*public void requestPasswordChangeByAdmin(String typedUserName, String newPassword, String oldPassword) throws UserAuthenticationException {
        for (User user : usersList) {
            if (isUserIDEqual(user, typedUserName)) {
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
    }*/

    public User getRecipientByUsername(String username){
        for(User recipient: usersList){
            if(username.equals(recipient.getUsername())){
                return recipient;
            }
        }
        return null;
    }

    public boolean isUserIdEqual(User existingUser, String userName) {
        return  existingUser.getUserId() == userName.hashCode();
    }

    public boolean ifPasswordEqual(int existingHashedPassword, String typedPassword) {
        return existingHashedPassword == typedPassword.hashCode();
    }

}
