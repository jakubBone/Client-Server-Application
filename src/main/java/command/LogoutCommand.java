package command;

import user.manager.UserManager;

public class LogoutCommand implements Command {
    private final UserManager userManager;

    public LogoutCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        return userManager.logoutAndGetResponse();
    }
}
