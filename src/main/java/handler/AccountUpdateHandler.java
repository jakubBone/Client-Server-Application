package handler;

import lombok.extern.log4j.Log4j2;
import shared.OperationResponses;
import user.User;
import user.UserManager;

@Log4j2
public class AccountUpdateHandler {

    public String getChangePasswordResponse(String username, String newPassword, UserManager userManager) {
        if (!userManager.ifCurrentUserAdmin()) {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", user.getUsername());
            return OperationResponses.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().changePassword(user, newPassword);
        log.info("Password changed successfully for user: {}", user.getUsername());
        return OperationResponses.OPERATION_SUCCEEDED.getResponse();
    }

    public String getUserDeleteResponse(String username, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", user.getUsername());
            return OperationResponses.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().deleteUser(user);
        log.info("User account deletion succeeded: {}", user.getUsername());
        return OperationResponses.OPERATION_SUCCEEDED.getResponse();
    }

    public String getChangeRoleResponse(String username, User.Role role, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            return OperationResponses.AUTHORIZATION_FAILED.getResponse();
        }

        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("Failed to find user: {}", user.getUsername());
            return OperationResponses.FAILED_TO_FIND_USER.getResponse();
        }

        userManager.getAdmin().changeUserRole(user, role);
        log.info("Role change succeeded: {}", user.getUsername());
        return OperationResponses.ROLE_CHANGE_SUCCEEDED.getResponse();
    }


}


