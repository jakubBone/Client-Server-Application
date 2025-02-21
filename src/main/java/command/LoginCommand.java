package command;

import ui.UserInput;
import user.manager.AuthManager;
import user.manager.UserManager;

public class LoginCommand implements Command {
    private final String command;
    private final String username;
    private final String password;
    private final AuthManager authManager;
    private final UserManager userManager;

    public LoginCommand(String command, String username, String password, AuthManager authManager, UserManager userManager) {
        this.command = command;
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
