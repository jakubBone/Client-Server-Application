package operations;

import lombok.extern.log4j.Log4j2;
import user.User;
import user.UserManager;

import java.io.IOException;

@Log4j2
public class AccountUpdateHandler {


    public String getUpdateResponse(String updateOperation, String userToUpdate, String newPassword,
                                    UserManager userManager)
            throws IOException {
        String response = null;
            switch (updateOperation) {
                case "PASSWORD":
                    response = changePassworAndGetResponse(userToUpdate, newPassword, userManager);
                    break;
                case "DELETE":
                    response = deleteAccountAndGetResponse(userToUpdate, userManager);
                    break;
            }
        return response;
    }


    public String changePassworAndGetResponse(String username, String newPassword,UserManager userManager)  {
        User userToUpdate = userManager.getUserByUsername(username);
        if (userToUpdate != null) {
            userManager.getAdmin().changePassword(userToUpdate, newPassword);
            log.info("Password changed successfully for user: {}", userToUpdate.getUsername());
            return userToUpdate.getUsername() + " password change successful";
        } else {
            log.warn("Failed to find user for update: {}", username);
            return "Update failed: " + username + " not found";
        }
    }

    public String deleteAccountAndGetResponse(String username, UserManager userManager) {
        if(userManager.ifCurrentUserAdmin()){
            log.info("Update failed: Cannot delete admin account");
            return "Update failed: Cannot delete admin account";
        } else {
            User userToUpdate = userManager.getUserByUsername(username);
            if (userToUpdate != null) {
                userManager.getAdmin().deleteUser(userToUpdate);
                log.info("User account deleted successfully: {}", userToUpdate.getUsername());
                return userToUpdate.getUsername() + " account deletion successful";
            } else {
                log.warn("Failed to find user for update: {}", username);
                return "Update failed: " + username + " not found";
            }
        }
    }
}

