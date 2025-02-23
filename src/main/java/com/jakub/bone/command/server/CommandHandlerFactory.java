package com.jakub.bone.command.server;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.server.ServerInfo;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.UserService;

@Log4j2
public class CommandHandlerFactory {
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerInfo serverInfo;

    public CommandHandlerFactory(AuthService authManager, UserService userManager, MailService mailService, ServerInfo serverInfo) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.serverInfo = serverInfo;
    }

    public CommandHandler createCommand(CommandDTO commandDTO)  {
        String command = commandDTO.getCommandType().toUpperCase();
        switch (command) {
            case "REGISTER", "LOGIN" -> { return new AuthHandler(authManager, userManager); }
            case "LOGOUT" -> { return new LogoutHandler(authManager); }
            case "HELP", "INFO", "UPTIME" -> { return new ServerInfoHandler(serverInfo); }
            case "NEW" -> { return new NewMailHandler(mailService, userManager); }
            case "INBOX" -> { return new InboxHandler(mailService); }
            case "SENT" -> { return new SentMailHandler(mailService); }
            case "DELETE" -> { return new DeleteMailHandler(mailService); }
            case "CHANGE", "REMOVE", "ROLE", "SWITCH" -> { return new EditProfileHandler(userManager); }
            default -> {
                log.warn("Unknown operation: {}", command);
                return null;
            }
        }
    }
}
