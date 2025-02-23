package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;

public class AuthHandler implements CommandHandler {
    private final AuthService authManager;
    private final UserService userManager;

    public AuthHandler(AuthService authManager, UserService userManager) {
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String username = (String) commandDTO.getPayload().get("username");
        String password = (String) commandDTO.getPayload().get("password");
        if ("REGISTER".equalsIgnoreCase(commandDTO.getCommandType())){
            return authManager.register(username, password, userManager);
        } else {
            return authManager.login(username, password, userManager);
        }
    }
}
