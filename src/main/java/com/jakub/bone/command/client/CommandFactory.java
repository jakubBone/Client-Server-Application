package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
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
        return switch (command.toUpperCase()) {
            case "LOGIN", "REGISTER" -> new AuthCommand(command, input);
            case "LOGOUT" -> new LogoutCommand();
            case "UPTIME", "INFO", "HELP" -> new ServerInfoCommand(command);
            case "NEW" -> new NewMailCommand(input);
            case "READ" -> new ReadMailCommand(input);
            case "DELETE" -> new DeleteMailCommand(input);
            case "EDIT" -> new EditProfileCommand(input);
            default -> {
                log.warn("Unknown operation: {}", command);
                yield null;
            }
        };
    }
}
