package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class LogoutCommand implements Command {
    private String command;

    public LogoutCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        return CommandDTO.builder()
                .commandType(command.toUpperCase())
                .build();
    }
}
