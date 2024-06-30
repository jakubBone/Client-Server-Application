package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.credential.User;
import user.manager.UserManager;

/**
 * The PasswordChangeHandler class handles user passwords changing by admin.
 * It ensures the current user is authorized to perform these operations.
 */

@Log4j2
public class PasswordChangeHandler {
    public String getResponse(String username, String newPassword, UserManager userManager) {
        log.info("Attempting to change password for user: {}", username);

        if(userManager.isUserAdmin()){
            User user = userManager.getUserByUsername(username);
            if (user == null) {
                log.warn("Failed to find user: {}", username);
                return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
            }

            userManager.changePassword(user, newPassword);

            log.info("Password changed successfully for user: {}", username);
            return ResponseMessage.OPERATION_SUCCEEDED.getResponse();
        } else {
            log.warn("Authorization failed for password change for user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }
    }
}