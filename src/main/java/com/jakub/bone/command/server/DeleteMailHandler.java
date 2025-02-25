package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.MailService;

import static com.jakub.bone.utils.ResponseStatus.*;

public class DeleteMailHandler implements CommandHandler {
    private final MailService mailService;

    public DeleteMailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String boxType = commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            return UNKNOWN_REQUEST.getResponse();
        }
        mailService.deleteMails(boxType);
        return MAIL_DELETION_SUCCEEDED.getResponse();
    }
}
