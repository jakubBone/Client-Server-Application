package response.user;

import response.Response;
import request.Request;
import service.UserService;
import utils.ResponseStatus;
import domain.User;

public class UserRemoveResponse implements Response {
    private final UserService userManager;

    public UserRemoveResponse(UserService userManager) {
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
