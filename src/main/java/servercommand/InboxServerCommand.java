package servercommand;

import command.CommandMessage;
import mail.Mail;
import mail.MailService;

import java.util.List;

public class InboxServerCommand {
    private final MailService mailService;

    public InboxServerCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String boxType = (String) commandMessage.getPayload().get("boxType");
        if (boxType == null) {
            boxType = "INBOX"; // domyślnie
        }
        List<Mail> mails = mailService.getMails(boxType);
        if (mails.isEmpty()) {
            return "Mailbox is empty";
        }
        StringBuilder response = new StringBuilder();
        for (Mail mail : mails) {
            response.append("From: ").append(mail.getSender().getUsername())
                    .append(" - Message: ").append(mail.getMessage()).append("\n");
        }
        return response.toString()
}
