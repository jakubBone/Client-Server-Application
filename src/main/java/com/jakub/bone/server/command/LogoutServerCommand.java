package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.application.UserService;

public class LogoutServerCommand implements ServerCommand {
    private final UserService userManager;

    public LogoutServerCommand(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        return userManager.logoutAndGetResponse();
    }
}
