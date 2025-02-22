package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;

public interface ServerCommand {
    String execute(CommandMessage commandMessage);
}
