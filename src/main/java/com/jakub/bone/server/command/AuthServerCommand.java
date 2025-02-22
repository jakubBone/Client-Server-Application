package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;

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
        if ("REGISTER".equalsIgnoreCase(commandMessage.getCommandType())){
            return authManager.register(username, password, userManager);
        } else {
            return authManager.login(username, password, userManager);
        }
    }
}
