package command;


public class InboxCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("INBOX")
                .build();
    }
}
