package handler.mail;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;

import java.io.IOException;
import java.util.List;
@Log4j2
public class MailboxHandler {
    private MailService mailService = new MailService();

    public String getResponse(String mailboxOperation, String boxType) throws IOException {
        log.info("Processing mailbox operation: {}", mailboxOperation);
        switch (mailboxOperation) {
            case "READ":
                return getReadResponse(boxType);
            case "EMPTY":
                return getEmptyMailboxResponse(boxType);
            default:
                log.warn("Unknown mail operation: {}", mailboxOperation);
                throw new IllegalArgumentException("Unknown mail operation: " + mailboxOperation);
        }
    }

    private String getReadResponse(String boxType) {
        log.info("Reading mails from box: {}", boxType);
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
        log.info("Emptying mails from box: {}", boxType);
        mailService.deleteEmails(boxType);
        return "Mails deletion succeeded";
    }
}
