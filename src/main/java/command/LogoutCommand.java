package command;

import user.manager.UserManager;

public class LogoutCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("LOGOUT")
                .build();
    }
}
