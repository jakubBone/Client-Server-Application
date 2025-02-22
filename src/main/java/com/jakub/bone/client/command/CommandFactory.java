package com.jakub.bone.client.command;

import lombok.extern.log4j.Log4j2;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

@Log4j2
public class CommandFactory {
    private final UserInput input;

    public CommandFactory(UserInput input) {
        this.input = input;
    }

    public Command createCommand(String command) throws IOException {
        switch (command.toUpperCase()) {
            case "LOGIN", "REGISTER" -> {
                return new AuthCommand(command, input);
            }
            case "LOGOUT" -> {
                return new LogoutCommand();
            }
            case "UPTIME", "INFO", "HELP" -> {
                return new ServerDetailsCommand(command);
            }
            case "NEW" -> {
                return new NewMailCommand(input);
            }
            case "INBOX" -> {
                return new InboxCommand();
            }
            case "SENT" -> {
                return new SentCommand(input);
            }
            case "DELETE" -> {
                return new DeleteMailCommand(input);
            }
            case "EDIT" -> {
                return new EditProfileCommand(input)
            }
            default:
                log.warn("Unknown operation: {}", command);
                return null;
        }
    }
}
