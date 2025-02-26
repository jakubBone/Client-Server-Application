package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

import static com.jakub.bone.ui.Screen.printMailboxScreen;

public class ReadMailCommand implements Command {
    private UserInput input;
    public ReadMailCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        printMailboxScreen();
        String boxType = input.getRequest().trim().toUpperCase();

        while(!isBoxTypeValid(boxType)){
            printMailboxScreen();
            boxType = input.getRequest().trim().toUpperCase();
        }
        return new CommandDTO.Builder()
                .commandType("READ")
                .addPayload("boxType", boxType)
                .build();
    }
    private boolean isBoxTypeValid(String boxType) {
        return (boxType != null && boxType.equalsIgnoreCase("INBOX")
                || boxType.equalsIgnoreCase("SENT"));
    }
}
