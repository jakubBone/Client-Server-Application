package handler.mail;

import java.io.IOException;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import shared.ResponseMessage;

/**
 * The MailboxHandler class handles mailbox operations such as reading and deleting mails.
 * It uses the MailService to perform the operations and generate appropriate responses.
 */

@Log4j2
public class MailboxHandler {
    private MailService mailService = new MailService();

    /**
     * Processes mailbox operations (READ, DELETE) and generates appropriate responses.
     * @param mailboxOperation The mailbox operation command (e.g., "READ", "DELETE")
     * @param boxType The type of mailbox (e.g., "UNREAD", "OPENED")
     */
    public String getResponse(String mailboxOperation, String boxType) throws IOException {
        log.info("Processing mailbox operation: {}", mailboxOperation);

        switch (mailboxOperation) {
            case "READ":
                return getReadResponse(boxType);
            case "DELETE":
                return getDeleteMailsResponse(boxType);
            default:
                log.warn("Unknown mail operation: {}", mailboxOperation);
                return ResponseMessage.UNKNOWN_REQUEST.getResponse();
        }
    }

    /**
     * Generates the response for reading mails from the specified mailbox.
     */
    private String getReadResponse(String boxType) {
        log.info("Reading mails from box: {}", boxType);
        List<Mail> mailsToRead = mailService.getMails(boxType);

        if (mailsToRead.isEmpty()) {
            return ResponseMessage.MAILBOX_EMPTY.getResponse();
        }

        // Mail reading
        StringBuilder response = new StringBuilder();
        for (Mail mail : mailsToRead) {
            response.append("From: ")
                    .append(mail.getSender().getUsername())
                    .append("\n Message: ")
                    .append(mail.getMessage())
                    .append("\n");
        }

        if(boxType.equals(Mail.Status.UNREAD.toString())){
            mailService.markAsRead();
        }

        return response.toString();
    }

    private String getDeleteMailsResponse(String boxType){
        log.info("Deleting mails from box: {}", boxType);
        mailService.deleteMails(boxType);
        return ResponseMessage.MAIL_DELETION_SUCCEEDED.getResponse();
    }
}