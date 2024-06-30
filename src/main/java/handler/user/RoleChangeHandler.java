package handler.user;
import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.credential.User;
import user.manager.UserManager;

/**
 * The RoleChangeHandler class handles user role changing by admin.
 * It ensures the current user is authorized to perform these operations.
 */

@Log4j2
public class RoleChangeHandler {
    public String getResponse(String username, User.Role role, UserManager userManager) {
        log.info("Attempting to change role for user: {}", username);

        if(userManager.isUserAdmin()){
            User user = userManager.getUserByUsername(username);
            if (user == null) {
                log.warn("Failed to find user: {}", username);
                return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
            }

            userManager.changeUserRole(user, role);

            log.info("Role change succeeded: {}", username);
            return ResponseMessage.ROLE_CHANGE_SUCCEEDED.getResponse();
        } else {
            log.warn("Authorization failed for password change for user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }
    }
}
