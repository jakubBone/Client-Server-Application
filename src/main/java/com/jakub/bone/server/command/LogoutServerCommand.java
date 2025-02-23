package com.jakub.bone.server.command;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.application.UserService;

public class LogoutServerCommand implements ServerCommand {
    private final AuthService authService;

    public LogoutServerCommand(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        return authService.logout();
    }
}
