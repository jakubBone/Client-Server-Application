package response.auth;

import response.Response;
import request.Request;
import user.manager.AuthManager;
import user.manager.UserManager;

public class LoginServerCommand implements Response {
    private final AuthManager authManager;
    private final UserManager userManager;

    public LoginServerCommand(AuthManager authManager, UserManager userManager) {
        this.authManager = authManager;
        this.userManager = userManager;
    }
    @Override
    public String execute(Request request) {
        return authManager.loginAndGetResponse(request.getUsername(), request.getPassword(), userManager);
    }
}