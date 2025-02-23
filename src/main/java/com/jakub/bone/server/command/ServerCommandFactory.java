package com.jakub.bone.server.command;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;
import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.server.ServerDetails;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.UserService;

@Log4j2
public class ServerCommandFactory {
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;

    public ServerCommandFactory(AuthService authManager, UserService userManager, MailService mailService, ServerDetails serverDetails) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.serverDetails = serverDetails;
    }

    public ServerCommand createCommand(CommandMessage commandMessage)  {
        String command = commandMessage.getCommandType().toUpperCase();
        switch (command) {
            case "REGISTER", "LOGIN" -> { return new AuthServerCommand(authManager, userManager); }
            case "LOGOUT" -> { return new LogoutServerCommand(authManager); }
            case "HELP", "INFO", "UPTIME" -> { return new ServerDetailsCommand(serverDetails); }
            case "NEW" -> { return new NewMailServerCommand(mailService, userManager); }
            case "INBOX" -> { return new InboxServerCommand(mailService); }
            case "SENT" -> { return new SentServerCommand(mailService); }
            case "DELETE" -> { return new DeleteMailServerCommand(mailService); }
            case "CHANGE", "REMOVE", "ROLE", "SWITCH" -> { return new EditServerCommand(userManager); }
            default -> {
                log.warn("Unknown operation: {}", command);
                return null;
            }
        }
    }
}
