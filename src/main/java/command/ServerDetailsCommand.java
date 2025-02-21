package command;

public class ServerDetailsCommand implements Command{
    private final String command;

    public ServerDetailsCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType(command) //  "UPTIME", "INFO" or "HELP"
                .build();
    }
}
