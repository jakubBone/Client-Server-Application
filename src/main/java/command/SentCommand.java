package command;


public class SentCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("SENT")
                .build();

    }
}
