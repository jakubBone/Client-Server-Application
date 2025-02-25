package com.jakub.bone.command.server;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.command.common.CommandDTO;

public class LogoutHandler implements CommandHandler {
    private final AuthService authService;

    public LogoutHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        return authService.logout();
    }
}
