package command;

public class DeleteMailCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("DELETE")
                .build();

    }
}
