package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.ConsolerReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthCommand implements Command {
    private final String request;

    public AuthCommand(String request) {
        this.request = request;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        ConsolerReader consolerReader = new ConsolerReader();
        String username = consolerReader.promptUsername();
        String password = consolerReader.promptPassword();

        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        return CommandDTO.builder()
                .commandType(request.toUpperCase())
                .payload(payload)
                .build();
    }
}
