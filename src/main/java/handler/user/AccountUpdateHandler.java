package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

/**
 * The AccountUpdateHandler class handles account update requests such as changing passwords, deleting users, and changing user roles.
 * It ensures the current user is authorized to perform these operations.
 */

@Log4j2
public class AccountUpdateHandler {

    /**
     * Attempts to change the password for the specified user.
     * Ensures the current user is an admin and the target user exists.
     * @param username The username of the user whose password is to be changed
     * @param newPassword The new password to be set
     * @param userManager The UserManager instance for managing users
     * @return The response message as a string
     */
    public String getChangePasswordResponse(String username, String newPassword, UserManager userManager) {
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

    /**
     * Attempts to delete the specified user account.
     * Ensures the current user is an admin and the target user exists.
     */
    public String getUserDeleteResponse(String username, UserManager userManager) {
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


    /**
     * Attempts to change the role for the specified user.
     * Ensures the current user is an admin and the target user exists.
     * @param role The new role to be assigned to the user
     */
    public String getChangeRoleResponse(String username, User.Role role, UserManager userManager) {
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