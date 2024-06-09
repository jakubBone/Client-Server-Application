package user;

import shared.JsonConverter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Admin extends User {

    JsonConverter jsonConverter = new JsonConverter();

    public Admin() {
        super("admin", "java10", Role.ADMIN);
        log.info("Admin instance created");
    }

    public void changePassword(User user, String newPassword){
        log.info("Changing password for user: {}", user.getUsername());
        user.setPassword(newPassword);
        jsonConverter.saveUserData(user);
        log.info("Password change succeeded for user: {}", user.getUsername());
    }

    public void deleteUser(User user){
        log.info("Deleting user: {}", user.getUsername());
        UserManager.usersList.remove(user);
        log.info("User deletion succeeded: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role){
        log.info("Changing role for user: {}", user.getUsername());
        user.setRole(User.Role.ADMIN);
        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }

    public void switchUser(User user) {
        log.info("Switching to user: {}", user.getUsername());
        UserManager.currentLoggedInUser = user;
        UserManager.ifAdminSwitched = true;
        log.info("Switched to user: {}", user.getUsername());
        }
}


