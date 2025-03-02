package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadMailCommand implements Command {
    private UserInput input;
    public ReadMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String boxType = null;
        while(!isBoxTypeValid(boxType)){
            boxType = input.promptMailbox();
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("boxType", boxType);

        return CommandDTO.builder()
                .commandType("READ")
                .payload(payload)
                .build();
    }

    private boolean isBoxTypeValid(String boxType) {
        return boxType != null && (boxType.equalsIgnoreCase("INBOX")
                || boxType.equalsIgnoreCase("SENT"));
    }
}
