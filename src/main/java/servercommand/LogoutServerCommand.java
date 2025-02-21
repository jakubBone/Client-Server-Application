package servercommand;

import command.CommandMessage;
import user.manager.UserManager;

public class LogoutServerCommand implements ServerCommand {
    private final UserManager userManager;

    public LogoutServerCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        return userManager.logoutAndGetResponse();
    }
}
