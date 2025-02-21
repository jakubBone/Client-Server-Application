package servercommand;

import user.manager.AuthManager;
import user.manager.UserManager;

public class LoginServerCommand {
    private final String username;
    private final String password;
    private final AuthManager authManager;
    private final UserManager userManager;

    public LoginServerCommand(String username, String password, AuthManager authManager, UserManager userManager) {
        this.username = username;
        this.password = password;
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        return authManager.loginAndGetResponse(username, password, userManager);
    }
}
