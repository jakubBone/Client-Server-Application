package user;

import shared.JsonConverter;

public class Admin extends User {

    JsonConverter jsonConverter = new JsonConverter();

    public Admin() {
        super("admin", "java10", Role.ADMIN);
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(newPassword);
        user.hashPassword();
        jsonConverter.saveUserData(user);
    }

    public void deleteUser(User user){
        UserManager.usersList.remove(user);
    }

    public void changeUserRole(User user, User.Role role){
        user.setRole(User.Role.ADMIN);
    }

    public void switchUser(User user) {
            UserManager.currentLoggedInUser = user;
            UserManager.ifAdminSwitched = true;
        }
}


