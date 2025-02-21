package servercommand;

import command.CommandMessage;
import user.manager.AuthManager;
import user.manager.UserManager;

public class AuthServerCommand implements ServerCommand {
    private final AuthManager authManager;
    private final UserManager userManager;

    public AuthServerCommand(AuthManager authManager, UserManager userManager) {
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String username = (String) commandMessage.getPayload().get("username");
        String password = (String) commandMessage.getPayload().get("password");
        if ("REGISTER".equals(commandMessage.getCommandType()){
            return authManager.registerAndGetResponse(username, password, userManager);
        } else {
            return authManager.loginAndGetResponse(username, password, userManager);
        }
    }
}
