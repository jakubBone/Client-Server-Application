package com.jakub.bone.client.command;

import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class NewMailCommand implements Command{

    private UserInput input;

    public NewMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandMessage buildCommandMessage() throws IOException {
        String recipient = input.promptRecipient();
        String message = input.promptMessage();
        return new CommandMessage.Builder()
                .commandType("NEW")
                .addPayload("recipient", recipient)
                .addPayload("message", message)
                .build();
    }
}
