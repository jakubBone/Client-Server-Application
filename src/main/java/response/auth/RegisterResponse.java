package response.auth;

import response.Response;
import request.Request;
import user.manager.AuthManager;
import user.manager.UserManager;
public class RegisterResponse implements Response {
    private final AuthManager authManager;
    private final UserManager userManager;

    public RegisterResponse(AuthManager authManager, UserManager userManager) {
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        return authManager.registerAndGetResponse(request.getUsername(), request.getPassword(), userManager);
    }
}
