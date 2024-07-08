package response.auth;

import response.Response;
import request.Request;
import user.manager.UserManager;
public class LogoutResponse implements Response {
    private final UserManager userManager;

    public LogoutResponse(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        return userManager.logoutAndGetResponse();
    }
}