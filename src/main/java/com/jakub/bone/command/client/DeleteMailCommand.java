package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class DeleteMailCommand implements Command {
    private UserInput input;

    public DeleteMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        Screen.printMailboxScreen();
        String boxType = input.getRequest().trim().toUpperCase();
        return new CommandDTO.Builder()
                .commandType("DELETE")
                .addPayload("boxType", boxType)
                .build();
    }
}
