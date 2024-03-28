package user;

import exceptions.UserAutenthactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private List<User> usersList;

    public UserManager() {
        this.usersList = new ArrayList<>();
    }

    public void register(String userName, String password) throws IllegalArgumentException{
        for (User user : usersList) {
            if(isUserNameEqual(user, userName))
                throw new IllegalArgumentException("User already exists");
        }
        String hashedPassword = hashPassword(password);
        usersList.add(new User(userName, hashedPassword, User.Role.USER));
        logger.info("Registration successful");
    }

    public void login(String userName, String password) throws UserAutenthactionException {
        for(User user: usersList) {
            if (isUserNameEqual(user, userName)) {
                if (!ifPasswordEqual(password,user.getHashedPassword())) {
                    logger.info("Incorrect password");
                } else {
                    user.isUserLoggedIn = true;
                    logger.info("Login successful");

                }
            } else
                throw new UserAutenthactionException("Login failed");
        }
    }

    public void logout(String userName) throws UserAutenthactionException{
        for(User user: usersList){
            if(!isUserNameEqual(user, userName)) {
                throw new UserAutenthactionException("Logout failed");
            }
        }
        logger.info("Logout successful");
    }

    public void reguestAccountRemovalbyAdmin(String userName, User.Role requiredRole) throws UserAutenthactionException{
        if(requiredRole == User.Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new UserAutenthactionException("User not found");
                } else {
                    usersList.remove(user);
                    logger.info("Account delete successful");
                }
            }
        } else {
            logger.info("No permission");
        }
    }

    public void requestPasswordChangeByAdmin(String userName, User.Role requiredRole, String newPassword, String oldPassword) throws UserAutenthactionException{
        if(requiredRole == User.Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new UserAutenthactionException("User not found");
                } else {
                    if(ifPasswordEqual(user.getHashedPassword(), hashPassword(oldPassword))) {
                        user.setHashedPassword(hashPassword(newPassword));
                        logger.info("Password change successful");
                    }
                }
            }
        } else {
            logger.info("No permission");
        }
    }

    public boolean isUserNameEqual(User user, String userName) {
        return user.getUsername().equals(userName);
    }
    public boolean ifPasswordEqual(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
    public String hashPassword(String password){
        return String.valueOf(password.hashCode());
    }
}
