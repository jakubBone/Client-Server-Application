package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewMailCommand implements Command {

    private UserInput input;

    public NewMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String recipient = input.promptRecipient();
        String message = input.promptMessage();

        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("recipient", recipient);
        payLoad.put("message", message);

        return CommandDTO.builder()
                .commandType("NEW")
                .payload(payLoad)
                .build();
    }
}
