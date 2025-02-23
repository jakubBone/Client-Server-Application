package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;

public interface CommandHandler {
    String execute(CommandDTO commandDTO);
}
