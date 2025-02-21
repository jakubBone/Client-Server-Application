package response.user;

import response.Response;
import request.Request;
import service.UserService;
import utils.ResponseStatus;
import domain.User;

public class UserSwitchResponse implements Response {
    private final UserService userManager;
    public UserSwitchResponse(UserService userManager) {
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

        if (UserService.ifSwitchedToAdminUser) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE.getResponse();
        }

        if(UserService.ifSwitchedToNonAdminUser) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE.getResponse();
        }

        return ResponseStatus.SWITCH_FAILED.getResponse();
    }
}

