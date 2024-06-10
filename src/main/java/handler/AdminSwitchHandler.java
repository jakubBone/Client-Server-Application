package handler;

import shared.OperationResponses;
import user.User;
import user.UserManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminSwitchHandler {

    public String getResponse(String username, UserManager userManager) {
        log.info("Attempting to switch admin to user: {}", username);
        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.warn("User not found: {}", username);
            return OperationResponses.SWITCH_FAILED.getResponse() + ": user not found";
        }

        if (!userManager.ifCurrentUserAdmin()) {
            log.warn("Non-admin user attempted switch: {}", username);
            return OperationResponses.SWITCH_FAILED.getResponse() + ": non-admin user";
        }

        userManager.getAdmin().switchUser(user);

        if (UserManager.ifAdminSwitched) {
            log.info("Switch succeeded: {}", username);
            return OperationResponses.SWITCH_SUCCEEDED.getResponse();
        } else {
            log.warn("Switch failed: {}", username);
            return OperationResponses.SWITCH_FAILED.getResponse();
        }
    }
}

