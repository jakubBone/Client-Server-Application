package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.ConsolerReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewMailCommand implements Command {

    private final ConsolerReader reader;

    public NewMailCommand(ConsolerReader reader) {
        this.reader = reader;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String recipient = reader.promptRecipient();
        String message = reader.promptMessage();

        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("recipient", recipient);
        payLoad.put("message", message);

        return CommandDTO.builder()
                .commandType("NEW")
                .payload(payLoad)
                .build();
    }
}
