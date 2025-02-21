package servercommand;

import command.CommandMessage;
import mail.MailService;

public class DeleteMailServerCommand implements ServerCommand {
    private final MailService mailService;

    public DeleteMailServerCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        // Odczytujemy typ skrzynki z payload, np. "INBOX" lub "SENT"
        String boxType = (String) commandMessage.getPayload().get("boxType");
        if (boxType == null || boxType.isEmpty()) {
            return "Brakuje parametru boxType.";
        }
        // Wywołanie logiki usuwania wiadomości w MailService
        mailService.deleteMails(boxType);
        return "Wiadomości ze skrzynki " + boxType + " zostały pomyślnie usunięte.";
    }
}
