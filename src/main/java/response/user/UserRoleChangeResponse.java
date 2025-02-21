package response.user;

import response.Response;
import request.Request;
import utils.ResponseStatus;
import domain.User;
import service.UserService;
public class UserRoleChangeResponse implements Response {
    private final UserService userManager;

    public UserRoleChangeResponse(UserService userManager) {
        this.userManager = userManager;
    }
    @Override
    public String execute(Request request) {
        if (userManager.isUserAdmin()) {
            User user = userManager.getUserByUsername(request.getUserToUpdate());

            if (user == null) {
                return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
            }

            userManager.changeUserRole(user, request.getNewRole());
            return ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse();
        } else {
            return ResponseStatus.AUTHORIZATION_FAILED.getResponse();
        }
    }
}