package com.jakub.bone.client.command;


public class InboxCommand implements Command {

    @Override
    public CommandMessage buildCommandMessage() {
        return new CommandMessage.Builder()
                .commandType("INBOX")
                .build();
    }
}
