package com.jakub.bone.client.command;

import com.jakub.bone.ui.UserInput;

public class AuthCommand implements Command {
    private final String command;
    private UserInput input;

    public AuthCommand(String command, UserInput input) {
        this.command = command;
        this.input = input;
    }

    @Override
    public CommandMessage buildCommandMessage() {
        UserInput userInput = new UserInput();
        String username = userInput.promptUsername();
        String password = userInput.promptPassword();
        return new CommandMessage.Builder()
                .commandType(command)
                .addPayload("username", username)
                .addPayload("password", password)
                .build();
    }
}
