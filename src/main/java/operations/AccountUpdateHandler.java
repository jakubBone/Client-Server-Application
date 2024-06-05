package operations;

import lombok.extern.log4j.Log4j2;
import user.User;
import user.UserManager;

import java.io.IOException;

@Log4j2
public class AccountUpdateHandler {

    UserManager userManager = new UserManager();


    public String getUpdateResponse(String updateOperation, String userToUpdate, String newPassword)
            throws IOException {
        String response = null;
            switch (updateOperation) {
                case "PASSWORD":
                    response = getPasswordChangeResponse(userToUpdate, newPassword);
                    break;
                case "DELETE":
                    response = getDeleteAccountResponse(userToUpdate);
                    break;
            }
        return response;
    }


    public String getPasswordChangeResponse(String username, String newPassword)  {
        User userToUpdate = userManager.getUserByUsername(username);
        if (userToUpdate != null) {
            userManager.changePassword(userToUpdate, newPassword);
            log.info("Password changed successfully for user: {}", userToUpdate.getUsername());
            return userToUpdate.getUsername() + " password change successful";
        } else {
            log.warn("Failed to find user for update: {}", username);
            return "Update failed: " + username + " not found";
        }
    }

    public String getDeleteAccountResponse(String username) {
        if(userManager.ifCurrentUserAdmin()){
            log.info("Update failed: Cannot delete admin account");
            return "Update failed: Cannot delete admin account";
        } else {
            User userToUpdate = userManager.getUserByUsername(username);
            if (userToUpdate != null) {
                userManager.deleteUser(userToUpdate);
                log.info("User account deleted successfully: {}", userToUpdate.getUsername());
                return userToUpdate.getUsername() + " account deletion successful";
            } else {
                log.warn("Failed to find user for update: {}", username);
                return "Update failed: " + username + " not found";
            }
        }
    }
}

