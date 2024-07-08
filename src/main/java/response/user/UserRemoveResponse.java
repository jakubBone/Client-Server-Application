package response.user;

import response.Response;
import request.Request;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;
public class UserRemoveResponse implements Response {
    private final UserManager userManager;

    public UserRemoveResponse(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        if (userManager.isUserAdmin()) {
            User user = userManager.getUserByUsername(request.getUserToUpdate());
            if (user == null) {
                return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
            }

            userManager.removeUser(user);
            return ResponseStatus.OPERATION_SUCCEEDED.getResponse();
        } else {
            return ResponseStatus.AUTHORIZATION_FAILED.getResponse();
        }
    }
}
