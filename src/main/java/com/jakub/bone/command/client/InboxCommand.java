package com.jakub.bone.command.client;


import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;

public class InboxCommand implements Command {

    @Override
    public CommandDTO buildCommandMessage() {
        return new CommandDTO.Builder()
                .commandType("INBOX")
                .build();
    }
}
