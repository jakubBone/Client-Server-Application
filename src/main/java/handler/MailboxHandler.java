package handler;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;

import java.io.IOException;
import java.util.List;
@Log4j2
public class MailboxHandler {
    private MailService mailService = new MailService();

    public String getResponse(String mailOperation, String boxType) throws IOException {
        if (mailOperation.equals("READ")) {
            return getReadResposne(boxType);
        } else if (mailOperation.equals("EMPTY")) {
            return getEmptyMailboxResponse(boxType);
        } else {
            throw new IllegalArgumentException("Unknown mail operation: " + mailOperation);
        }
    }

    private String getReadResposne(String boxType) {
        List<Mail> mailsToRead = mailService.getMailsToRead(boxType);

        if (mailsToRead.isEmpty()) {
            return "Mailbox is empty";
        }

        StringBuilder response = new StringBuilder();
        for (Mail mail : mailsToRead) {
            response.append("From ")
                    .append(mail.getSender().getUsername())
                    .append("\n Message: ")
                    .append(mail.getMessage());
        }

        mailService.markMailsAsRead(boxType);
        return response.toString();
    }

    private String getEmptyMailboxResponse(String boxType){
        mailService.deleteEmails(boxType);
        return "Mails deletion succeeded";
    }
}
