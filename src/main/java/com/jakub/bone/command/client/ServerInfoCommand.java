package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;

public class ServerInfoCommand implements Command {
    private final String request;

    public ServerInfoCommand(String request) {
        this.request = request;
    }

    @Override
    public CommandDTO buildCommandMessage() {
        return CommandDTO.builder()
                .commandType(request.toUpperCase()) //  "UPTIME", "INFO" or "HELP"
                .build();
    }
}
