package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.application.MailService;

import java.util.List;

public class SentMailHandler implements CommandHandler {
    private final MailService mailService;

    public SentMailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        List<Mail> mails = mailService.getMails("SENT");
        if (mails.isEmpty()) {
            return "No sent mails.";
        }
        StringBuilder response = new StringBuilder("Sent Mails:\n");
        for (Mail mail : mails) {
            response.append("To: ").append(mail.getRecipient().getUsername())
                    .append(" - Message: ").append(mail.getMessage()).append("\n");
        }
        return response.toString();
    }
}
