package handler.user;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.credential.User;
import user.manager.UserManager;

/**
 * The AdminSwitchHandler class handles requests to switch the any user account.
 * The operation permitted only for admin.
 * It verifies the current user is authorized to perform the switch.
 */

@Log4j2
public class UserSwitchHandler {

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

        userManager.switchUser(user);

        if (UserManager.ifAdminSwitched) {
            log.info("Switch succeeded: {}", username);
            return ResponseMessage.SWITCH_SUCCEEDED.getResponse();
        } else {
            log.warn("Switch failed: {}", username);
            return ResponseMessage.SWITCH_FAILED.getResponse();
        }
    }
}