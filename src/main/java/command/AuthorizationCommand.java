package command;

import user.manager.AuthManager;
import user.manager.UserManager;

public class AuthorizationCommand implements Command{
    private final String command;
    private final String username;
    private final String password;
    private final AuthManager authManager;
    private final UserManager userManager;

    public AuthorizationCommand(String command, String username, String password, AuthManager authManager, UserManager userManager) {
        this.command = command;
        this.username = username;
        this.password = password;
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        if(command.equals("REGISTER")){
            return authManager.registerAndGetResponse(username, password, userManager);
        } else{
            return authManager.loginAndGetResponse(username, password, userManager);
        }
    }
}
