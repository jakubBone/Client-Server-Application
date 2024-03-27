package user;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> usersList;

    public UserManager() {
        usersList = new ArrayList<>();
    }

    public void register(String userName, String password) {
        for (User user : usersList) {
            if(isUserNameEqual(user, userName))
                throw new IllegalArgumentException("User already exists");
        }
        String hashedPassword = hashPassword(password);
        usersList.add(new User(userName, hashedPassword, User.Role.USER));
        System.out.println("Registration successful");
    }

    public void login(String userName, String password){
        for(User user: usersList) {
            if (isUserNameEqual(user, userName)) {
                if (!ifPasswordEqual(password,user.getHashedPassword())) {
                    System.out.println("Incorrect password");
                } else {
                    System.out.println("Login successful");
                }
            } else
                throw new RuntimeException("Login failed");
        }
    }

    public void logout(String userName){
        for(User user: usersList){
            if(!isUserNameEqual(user, userName)) {
                throw new RuntimeException("Logout failed");
            }
        }
        System.out.println("Logout successful");
    }

    public void reguestAccountRemovalbyAdmin(String userName, User.Role requiredRole){
        if(requiredRole == User.Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new RuntimeException("User not found");
                } else {
                    usersList.remove(user);
                    System.out.println("Account delete successful");
                }
            }
        } else {
            System.out.println("No permission");
        }
    }
    public void requestPasswordChangeByAdmin(String userName, User.Role requiredRole, String newPassword, String oldPassword){
        if(requiredRole == User.Role.ADMIN) {
            for (User user : usersList) {
                if (!isUserNameEqual(user, userName)) {
                    throw new RuntimeException("User not found");
                } else {
                    if(ifPasswordEqual(user.getHashedPassword(), hashPassword(oldPassword))) {
                        user.setHashedPassword(hashPassword(newPassword));
                        System.out.println("Password change successful")
                    }
                }
            }
        } else {
            System.out.println("No permission");
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
