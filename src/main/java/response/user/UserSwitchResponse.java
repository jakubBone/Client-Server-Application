package response.user;

import response.Response;
import request.Request;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

public class UserSwitchResponse implements Response {
    private final UserManager userManager;
    public UserSwitchResponse(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        User user = userManager.getUserByUsername(request.getUserToSwitch());

        if (user == null) {
            return ResponseStatus.SWITCH_FAILED.getResponse() + ": user not found";
        }

        if (!userManager.isUserAdmin()) {
            return ResponseStatus.SWITCH_FAILED.getResponse() + ": user not authorized";
        }

        userManager.switchUser(user);

        if (UserManager.ifAdminSwitchedAndAuthorized) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE.getResponse();
        }

        if(UserManager.ifAdminSwitched) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE.getResponse();
        }

        return ResponseStatus.SWITCH_FAILED.getResponse();
    }
}

