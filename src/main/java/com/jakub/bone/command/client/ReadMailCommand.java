package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

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
        return new CommandDTO.Builder()
                .commandType("READ")
                .addPayload("boxType", boxType)
                .build();
    }

    private boolean isBoxTypeValid(String boxType) {
        return boxType != null && (boxType.equalsIgnoreCase("INBOX")
                || boxType.equalsIgnoreCase("SENT"));
    }
}
