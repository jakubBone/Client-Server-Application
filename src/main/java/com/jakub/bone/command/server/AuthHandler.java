package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;

public class AuthHandler implements CommandHandler {
    private final AuthService authService;
    private final UserService userService;

    public AuthHandler(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String username = commandDTO.getPayload().get("username");
        String password = commandDTO.getPayload().get("password");
        if ("REGISTER".equalsIgnoreCase(commandDTO.getCommandType())){
            return authService.register(username, password);
        } else {
            return authService.login(username, password);
        }
    }
}
