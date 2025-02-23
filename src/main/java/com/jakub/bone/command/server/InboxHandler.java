package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.application.MailService;


import java.util.List;
public class InboxHandler implements CommandHandler {
    private final MailService mailService;

    public InboxHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        // Pobieramy typ skrzynki z payload; domyślnie przyjmujemy "INBOX"
        String boxType = (String) commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            boxType = "INBOX";
        }
        List<Mail> mails = mailService.getMails(boxType);
        if (mails.isEmpty()) {
            return "Brak wiadomości w skrzynce " + boxType + ".";
        }
        StringBuilder response = new StringBuilder("Wiadomości w skrzynce " + boxType + ":\n");
        for (Mail mail : mails) {
            response.append("Od: ").append(mail.getSender().getUsername()).append("\n")
                    .append("Treść: ").append(mail.getMessage()).append("\n\n");
        }
        return response.toString();
    }
}