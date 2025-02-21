package command;

import ui.UserInput;
import user.manager.AuthManager;
import user.manager.UserManager;

import java.io.IOException;

public class RegisterCommand implements Command{
    private final String username;
    private final String password;
    private final AuthManager authManager;
    private final UserManager userManager;

    public RegisterCommand(String username, String password, AuthManager authManager, UserManager userManager) {
        this.username = username;
        this.password = password;
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        return authManager.registerAndGetResponse(username, password, userManager);
    }
}
