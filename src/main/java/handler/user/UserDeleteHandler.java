package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.credential.User;
import user.manager.UserManager;

/**
 * The UserDeleteHandler class handles user account deletion by admin.
 * It ensures the current user is authorized to perform these operations.
 */

@Log4j2
public class UserDeleteHandler {
    public String getResponse(String username, UserManager userManager) {
        log.info("Attempting to delete user: {}", username);

        if(userManager.isUserAdmin()){
            User user = userManager.getUserByUsername(username);

            if (user == null) {
                log.warn("Failed to find user: {}", username);
                return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
            }

            userManager.deleteUser(user);

            log.info("User account deletion succeeded: {}", username);
            return ResponseMessage.OPERATION_SUCCEEDED.getResponse();
        } else {
            log.warn("Authorization failed for password change for user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }
    }
}
