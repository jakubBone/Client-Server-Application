package command;

public class LogoutCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("LOGOUT")
                .build();
    }
}
