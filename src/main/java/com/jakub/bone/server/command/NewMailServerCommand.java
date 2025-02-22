package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.application.service.MailService;
import com.jakub.bone.application.service.UserService;

public class NewMailServerCommand implements ServerCommand {
    private final MailService mailService;
    private final UserService userManager;

    public NewMailServerCommand(MailService mailService, UserService userManager) {
        this.mailService = mailService;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String recipient = (String) commandMessage.getPayload().get("recipient");
        String message = (String) commandMessage.getPayload().get("message");
        return mailService.sendMail(userManager.getUserByUsername(recipient), message);
    }
}
