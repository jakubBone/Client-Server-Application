package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

/**
 * The AdminSwitchHandler class handles requests to switch the current user to an admin user.
 * It verifies the current user is authorized to perform the switch and updates the UserManager accordingly.
 */

@Log4j2
public class AdminSwitchHandler {

    /**
     * Attempts to switch the current user to an admin user.
     * Ensures the user exists and the current user is authorized to perform the switch.
     * @param username The username of the user to switch to
     * @param userManager The UserManager instance for managing users
     * @return The response message as a string
     */
    public String getResponse(String username, UserManager userManager) {
        log.info("Attempting to switch admin to user: {}", username);
        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("User not found: {}", username);
            return ResponseMessage.SWITCH_FAILED.getResponse() + ": user not found";
        }

        if (!userManager.isUserAdmin()) {
            log.warn("Non-admin user attempted switch: {}", username);
            return ResponseMessage.SWITCH_FAILED.getResponse() + ": non-admin user";
        }

        userManager.getAdmin().switchUser(user);

        if (UserManager.ifAdminSwitched) {
            log.info("Switch succeeded: {}", username);
            return ResponseMessage.SWITCH_SUCCEEDED.getResponse();
        } else {
            log.warn("Switch failed: {}", username);
            return ResponseMessage.SWITCH_FAILED.getResponse();
        }
    }
}