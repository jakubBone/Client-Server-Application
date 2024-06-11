package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

@Log4j2
public class AccountUpdateHandler {

    public String getChangePasswordResponse(String username, String newPassword, UserManager userManager) {
        log.info("Attempting to change password for user: {}", username);
        if (!userManager.ifCurrentUserAdmin()) {
            log.warn("Authorization failed for password change for user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", username);
            return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().changePassword(user, newPassword);
        log.info("Password changed successfully for user: {}", username);
        return ResponseMessage.OPERATION_SUCCEEDED.getResponse();
    }

    public String getUserDeleteResponse(String username, UserManager userManager) {
        log.info("Attempting to delete user: {}", username);
        if (!userManager.ifCurrentUserAdmin()) {
            log.warn("Authorization failed for deleting user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", username);
            return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().deleteUser(user);
        log.info("User account deletion succeeded: {}", username);
        return ResponseMessage.OPERATION_SUCCEEDED.getResponse();
    }

    public String getChangeRoleResponse(String username, User.Role role, UserManager userManager) {
        log.info("Attempting to change role for user: {}", username);
        if (!userManager.ifCurrentUserAdmin()) {
            log.warn("Authorization failed for changing role for user: {}", username);
            return ResponseMessage.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", username);
            return ResponseMessage.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().changeUserRole(user, role);
        log.info("Role change succeeded: {}", username);
        return ResponseMessage.ROLE_CHANGE_SUCCEEDED.getResponse();
    }
}


