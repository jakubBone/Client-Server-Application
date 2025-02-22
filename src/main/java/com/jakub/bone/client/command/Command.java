package com.jakub.bone.client.command;

import java.io.IOException;

public interface Command {
    CommandMessage buildCommandMessage() throws IOException;
}
