package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.application.MailService;
import com.jakub.bone.command.server.CommandHandler;

public class DeleteMailHandler implements CommandHandler {
    private final MailService mailService;

    public DeleteMailHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        // Odczytujemy typ skrzynki z payload, np. "INBOX" lub "SENT"
        String boxType = commandDTO.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            return "Brakuje parametru boxType.";
        }
        // Wywołanie logiki usuwania wiadomości w MailService
        mailService.deleteMails(boxType);
        return "Wiadomości ze skrzynki " + boxType + " zostały pomyślnie usunięte.";
    }
}
