package servercommand;

import command.CommandMessage;

public interface ServerCommand {
    String execute(CommandMessage commandMessage);
}
