package utils;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> usersList;

    public UserManager() {
        usersList = new ArrayList<>();
    }

    private String register(String userName, String password) {
        for (User user : usersList) {
            if(isUserNameEqual(user, userName))
                throw new IllegalArgumentException("User already exists");
        }
        String hashedPassword = hashPassword(password);
        usersList.add(new User(userName, hashedPassword, Role.USER));
        return "Registration successful";
    }

    private String login(String userName, String password){
        for(User user: usersList) {
            if (isUserNameEqual(user, userName)) {
                if (!ifPasswordEqual(password,user.getHashedPassword())) {
                    System.out.println("Incorrect password");
                }
            } else
                throw new RuntimeException("Login failed");
        }
        return "Login successful";
    }

    private String logout(String userName){
        for(User user: usersList){
            if(!isUserNameEqual(user, userName)) {
                throw new RuntimeException("Logout failed");
            }
        }
        return "Logout successful";
    }

    private String reguestAccountRemovalbyAdmin(String userName, Role requiredRole){
        if(requiredRole == Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new RuntimeException("User not found");
                } else {
                    usersList.remove(user);
                }
            }
        } else {
            return "No permission";
        }
        return "Account delete successful";
    }
    private String requestPasswordChangeByAdmin(String userName, Role requiredRole, String newPassword, String oldPassword){
        if(requiredRole == Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new RuntimeException("User not found");
                } else {
                    if(ifPasswordEqual(user.getHashedPassword(), hashPassword(oldPassword))) {
                        user.setHashedPassword(hashPassword(newPassword));
                        System.out.println("Password changed to:" + newPassword);
                    }
                }
            }
        } else {
            return "No permission";
        }
        return "Password change successful";
    }

    private boolean isUserNameEqual(User user, String userName) {
        return user.getUsername().equals(userName);
    }
    private boolean ifPasswordEqual(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
    private String hashPassword(String password){
        return String.valueOf(password.hashCode());
    }

}
