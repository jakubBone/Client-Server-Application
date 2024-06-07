package operations;

import lombok.extern.log4j.Log4j2;
import user.User;
import user.UserManager;

@Log4j2
public class AccountUpdateHandler {

    public String getChangePasswordResponse(String username, String newPassword, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            User user = userManager.getUserByUsername(username);
            if (user != null) {
                userManager.getAdmin().changePassword(user, newPassword);
                log.info("Password changed successfully for user: {}", user.getUsername());
                return OperationResponses.OPERATION_SUCCEEDED.getResponse();
            } else {
                log.warn("Failed to find user: {}", user.getUsername());
                return OperationResponses.FAILED_TO_FIND_USER.getResponse();
            }
        } else {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }
    }

    public String getUserDeleteResponse(String username, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            User user = userManager.getUserByUsername(username);
            if (user != null) {
                userManager.getAdmin().deleteUser(user);
                log.info("User account deletion succeeded: {}", user.getUsername());
                return OperationResponses.OPERATION_SUCCEEDED.getResponse();
            } else {
                log.warn("Failed to find user: {}", user.getUsername());
                return OperationResponses.FAILED_TO_FIND_USER.getResponse();
            }
        } else {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }
    }

    public String getChangeRoleResponse(String username, User.Role role, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            User user = userManager.getUserByUsername(username);
            if (user != null) {
                userManager.getAdmin().changeUserRole(user, role);
                log.info("Role change succeeded: {}", user.getUsername());
                return OperationResponses.ROLE_CHANGE_SUCCEEDED.getResponse();
            } else {
                log.warn("Failed to find user: {}", user.getUsername());
                return OperationResponses.ROLE_CHANGE_FAILED.getResponse();
            }
        } else {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }
    }
}

