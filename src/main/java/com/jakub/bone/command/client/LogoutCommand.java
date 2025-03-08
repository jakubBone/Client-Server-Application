package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;

import java.io.IOException;

public class LogoutCommand implements Command {

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        return CommandDTO.builder()
                .commandType("LOGOUT")
                .build();
    }
}
