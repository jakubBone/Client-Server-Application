package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.command.server.CommandHandler;

public class NewMailHandler implements CommandHandler {
    private final MailService mailService;
    private final UserService userManager;

    public NewMailHandler(MailService mailService, UserService userManager) {
        this.mailService = mailService;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String recipient = (String) commandDTO.getPayload().get("recipient");
        String message = (String) commandDTO.getPayload().get("message");
        return mailService.sendMail(userManager.getUserByUsername(recipient), message);
    }
}
