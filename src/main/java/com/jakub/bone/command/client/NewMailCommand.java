package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class NewMailCommand implements Command {

    private UserInput input;

    public NewMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String recipient = input.promptRecipient();
        String message = input.promptMessage();
        return new CommandDTO.Builder()
                .commandType("NEW")
                .addPayload("recipient", recipient)
                .addPayload("message", message)
                .build();
    }
}
