package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;

public class ServerInfoCommand implements Command {
    private final String command;

    public ServerInfoCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandDTO buildCommandMessage() {
        return new CommandDTO.Builder()
                .commandType(command.toUpperCase()) //  "UPTIME", "INFO" or "HELP"
                .build();
    }
}
