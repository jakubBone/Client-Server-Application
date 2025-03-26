package com.jakub.bone.command.client;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.common.Command;
import com.jakub.bone.ui.ConsolerReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchMessageCommand implements Command {
    private final ConsolerReader reader;

    public SearchMessageCommand(ConsolerReader reader) {
        this.reader = reader;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String boxType = null;
        String messageContent = null;
        while(!isBoxTypeValid(boxType)){
            boxType = reader.promptMailbox();
            messageContent = reader.promptMessageContent();
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("boxType", boxType);
        payload.put("messageContent", messageContent);

        return CommandDTO.builder()
                .commandType("SEARCH")
                .payload(payload)
                .build();
    }

    private boolean isBoxTypeValid(String boxType) {
        return boxType != null && (boxType.equalsIgnoreCase("INBOX")
                || boxType.equalsIgnoreCase("SENT"));
    }

}
