package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthCommand implements Command {
    private final String command;
    private UserInput input;

    public AuthCommand(String command, UserInput input) {
        this.command = command;
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        UserInput userInput = new UserInput();
        String username = userInput.promptUsername();
        String password = userInput.promptPassword();

        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        return CommandDTO.builder()
                .commandType(command.toUpperCase())
                .payload(payload)
                .build();
    }
}
