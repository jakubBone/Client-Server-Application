package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.ConsolerReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchCommand implements Command {
    private final ConsolerReader reader;

    public ElasticSearchCommand(ConsolerReader reader) {
        this.reader = reader;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String query = reader.promptMessageContent();
        Map<String, String> payload = new HashMap<>();
        payload.put("messageContent", query);
        return CommandDTO.builder()
                .commandType("SEARCH")
                .payload(payload)
                .build();
    }
}
