package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.MailService;
import com.jakub.bone.utils.ResponseStatus;

import java.util.List;

public class DeleteMailHandler implements CommandHandler {
    private final MailService mailService;

    public DeleteMailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String boxType = commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            return ResponseStatus.UNKNOWN_REQUEST.getResponse();
        }
        mailService.deleteMails(boxType);
        return ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse();
    }
}
