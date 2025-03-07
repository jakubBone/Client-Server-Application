package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

@Log4j2
public class CommandFactory {

    public CommandDTO createCommand(Command command) throws IOException {
        return command.buildCommandMessage();
    }
}
