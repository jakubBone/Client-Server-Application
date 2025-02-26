package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.domain.User;
import com.jakub.bone.utils.ResponseStatus;

public class NewMailHandler implements CommandHandler {
    private final MailService mailService;
    private final UserService userService;

    public NewMailHandler(MailService mailService, UserService userService) {
        this.mailService = mailService;
        this.userService = userService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String recipient = commandDTO.getPayload().get("recipient");
        String message = commandDTO.getPayload().get("message");

        User user = userService.findUserByUsername(recipient);
        if(user == null){
            return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
        }

        return mailService.sendMail(userService.findUserByUsername(recipient), message);
    }
}