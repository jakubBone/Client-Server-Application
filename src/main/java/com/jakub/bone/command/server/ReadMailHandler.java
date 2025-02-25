package com.jakub.bone.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.domain.Mail;
import static com.jakub.bone.utils.ResponseStatus.*;

import java.util.List;

public class ReadMailHandler implements CommandHandler {
    private final MailService mailService;
    public ReadMailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String boxType = commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            return UNKNOWN_REQUEST.getResponse();
        }

        List<Mail> mails = mailService.getMails(boxType);
        if (mails.isEmpty()) {
            return MAILBOX_EMPTY.getResponse();
        }

        StringBuilder response = new StringBuilder("\nMessages (" + boxType + "):\n\n");
        for (Mail mail : mails) {
            if("SENT".equals(boxType)){
                response.append("To: ").append(mail.getRecipient().getUsername()).append("\n");
            } else {
                response.append("From: ").append(mail.getSender().getUsername()).append("\n");
            }
            response.append(" Message: ").append(mail.getMessage()).append("\n");
        }
        return response.toString();
    }
}
