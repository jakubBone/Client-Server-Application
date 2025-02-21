package response.auth;

import response.Response;
import user.manager.AuthManager;
import user.manager.UserManager;

public class AuthServerCommand {
    private final String command;
    private final UserManager userManager;
    private final AuthManager authManager;

    public LoginServerCommand(String command, AuthManager authManager, UserManager userManager) {
        this.command = command;
        this.userManager = userManager;
        this.authManager = authManager;
    }
    @Override
    public String execute() {
        return authManager.loginAndGetResponse(request.getUsername(), request.getPassword(), userManager);
    }
}
