package operations;

import user.User;
import user.UserManager;

public class AdminSwitchUserHandler {

    public String getResponse(String username, UserManager userManager) {
        User user = userManager.getUserByUsername(username);
        if (user != null) {
            if (userManager.ifCurrentUserAdmin()) {
                userManager.getAdmin().switchUser(user);
                if (UserManager.ifAdminSwitched) {
                    return OperationResponses.SWITCH_SUCCEEDED.getResponse();
                } else {
                    return OperationResponses.SWITCH_FAILED.getResponse() + ": non-admin user";
                }
            }
        }
        return OperationResponses.SWITCH_FAILED.getResponse() + ": user not found";
    }
}
