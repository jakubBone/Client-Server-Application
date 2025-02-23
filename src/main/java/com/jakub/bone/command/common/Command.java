package com.jakub.bone.command.common;

import java.io.IOException;

public interface Command {
    CommandDTO buildCommandMessage() throws IOException;
}
