package operations;

import user.UserManager;

public class AdminSwitchUserHandler {
    public String getSwitchResponse(String username, UserManager userManager) {
        if (userManager.ifCurrentUserAdmin()) {
            userManager.switchUser(username);
            if (userManager.ifAdminSwitched) {
                return "Switch operation succeeded: Authorized";
            } else {
                return "Switch operation failed: User not found";
            }
        } else {
            return "Switch operation failed: Not authorized";
        }
    }
}
