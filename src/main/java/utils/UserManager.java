package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private List<User> usersList;
    private Map<String, String> userSession;
    private boolean ifSessionInit;

    public UserManager() {
        usersList = new ArrayList<>();
        userSession = new HashMap<>();
    }

    private String registerUser(String userName, String password) {
        for (User user : usersList) {
            if (user.getUsername().equals(userName)) {
                throw new IllegalArgumentException("User already exists");
            }
            String hashedPassword = hashPassword(password);
            usersList.add(new User(userName, hashedPassword, "user"));
        }
        return "User registered successfully";
    }

    private String loginUser(String userName, String password){
        for(User user: usersList) {
            if (user.getUsername().equals(userName)) {
                if (checkPassword(password, user.getHashedPassword())) {
                    ifSessionInit = true;
                }
                else
                    System.out.println("Incorrect password");
            }
            throw new RuntimeException("Login failed");
        }
        return "User logged successfully";
    }

    private String logoutUser(String userName){
        for(User user: usersList){
            if(user.getUsername().equals(userName) && ifSessionInit == true)
                ifSessionInit = false;
            else
                throw new RuntimeException("Logout failed");
        }
        return "User logout successfully";
    }

    private String removeUser(String userName){
        for(User user: usersList) {
            if (user.getUsername().equals(userName)) {
                usersList.remove(user);
            }
            throw new RuntimeException("Authorization failed or user not found.");
        }
        return "User deleted successfully";
    }

    private String resetPassword(String userName, String password){
        for(User user: usersList) {
            if (user.getUsername().equals(userName)) {
                // request to admin for change the password
            }
            throw new RuntimeException("           ");
        }
        return "Password reset successfully";
    }

    public String hashPassword(String password){
        return String.valueOf(password.hashCode());
    }
    private boolean checkPassword(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }

}
