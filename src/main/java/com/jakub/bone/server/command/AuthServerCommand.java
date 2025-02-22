package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.application.service.AuthService;
import com.jakub.bone.application.service.UserService;

public class AuthServerCommand implements ServerCommand {
    private final AuthService authManager;
    private final UserService userManager;

    public AuthServerCommand(AuthService authManager, UserService userManager) {
        this.authManager = authManager;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String username = (String) commandMessage.getPayload().get("username");
        String password = (String) commandMessage.getPayload().get("password");
        if ("REGISTER".equals(commandMessage.getCommandType()){
            return authManager.registerAndGetResponse(username, password, userManager);
        } else {
            return authManager.loginAndGetResponse(username, password, userManager);
        }
    }
}
