package user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    public static List<User> usersList;
    public static User currentLoggedInUser;
    public Admin admin;

    public UserManager() {
        usersList = new ArrayList<>();
        this.admin = new Admin();
        usersList.add(admin);
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
            User newUser = new User(typedUserName, typedPassword, User.Role.USER);
            usersList.add(newUser);
            currentLoggedInUser = newUser;
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

    public void logoutCurrentUser() {
        currentLoggedInUser = null;
    }

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

    public User findUserOnTheList(String username){
        User searchedUser = null;
        System.out.println("Userlist " + usersList);
        for (User user : usersList) {
            if (username.equals(user.getUsername())) {
                searchedUser = user;
                break;
            }
        }
        System.out.println("Username " + searchedUser.getUsername());
        return searchedUser;
    }

    public boolean isAdmin(){
        return currentLoggedInUser.role.equals(User.Role.ADMIN);
    }


}
