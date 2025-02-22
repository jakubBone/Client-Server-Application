package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.domain.model.Mail;
import com.jakub.bone.application.MailService;

import java.util.List;

public class SentServerCommand implements ServerCommand {
    private final MailService mailService;

    public SentServerCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
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
