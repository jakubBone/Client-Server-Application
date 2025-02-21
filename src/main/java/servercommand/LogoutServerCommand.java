package servercommand;

import command.CommandMessage;
import service.UserService;

public class LogoutServerCommand implements ServerCommand {
    private final UserService userManager;

    public LogoutServerCommand(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        return userManager.logoutAndGetResponse();
    }
}
