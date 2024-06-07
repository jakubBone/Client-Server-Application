package handler;

import shared.OperationResponses;
import user.User;
import user.UserManager;

public class AdminSwitchHandler {

    public String getResponse(String username, UserManager userManager) {
        User user = userManager.getUserByUsername(username);

        if (user == null) {
            return OperationResponses.SWITCH_FAILED.getResponse() + ": user not found";
        }

        if (!userManager.ifCurrentUserAdmin()) {
            return OperationResponses.SWITCH_FAILED.getResponse() + ": non-admin user";
        }

        userManager.getAdmin().switchUser(user);

        if (UserManager.ifAdminSwitched) {
            return OperationResponses.SWITCH_SUCCEEDED.getResponse();
        } else {
            return OperationResponses.SWITCH_FAILED.getResponse();
        }
    }
}

