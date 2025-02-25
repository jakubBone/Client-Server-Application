package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.domain.Mail;
import com.jakub.bone.application.MailService;

import java.util.List;

import static com.jakub.bone.utils.ResponseStatus.MAILBOX_EMPTY;

public class InboxHandler implements CommandHandler {
    private final MailService mailService;

    public InboxHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String boxType = commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            boxType = "INBOX";
        }
        List<Mail> mails = mailService.getMails(boxType);
        if (mails.isEmpty()) {
            return MAILBOX_EMPTY.getResponse();
        }
        StringBuilder response = new StringBuilder("Inbox messages (" + boxType + "):\n");
        for (Mail mail : mails) {
            response.append("From: ").append(mail.getSender().getUsername()).append("\n")
                    .append(" Message: ").append(mail.getMessage()).append("\n");
        }
        return response.toString();
    }
}