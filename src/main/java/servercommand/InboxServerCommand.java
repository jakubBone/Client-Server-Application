package servercommand;

import command.CommandMessage;
import mail.Mail;
import mail.MailService;


import java.util.List;
public class InboxServerCommand implements ServerCommand {
    private final MailService mailService;

    public InboxServerCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        // Pobieramy typ skrzynki z payload; domyślnie przyjmujemy "INBOX"
        String boxType = (String) commandMessage.getPayload().get("boxType");
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