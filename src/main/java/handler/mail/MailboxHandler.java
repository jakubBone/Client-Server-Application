package handler.mail;

import java.io.IOException;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import shared.ResponseMessage;
import user.UserManager;

/*
 * The MailboxHandler class handles mailbox operations such as reading and deleting mails.
 * It uses the MailService to perform the operations and generate appropriate responses.
 */
@Log4j2
public class MailboxHandler {
    private MailService mailService = new MailService();

    public String getResponse(String mailboxOperation, String boxType, UserManager userManager) throws IOException {
        log.info("Processing mailbox operation: {}", mailboxOperation);

        switch (mailboxOperation) {
            case "READ":
                return getReadResponse(boxType, userManager);
            case "DELETE":
                return getDeleteMailsResponse(boxType, userManager);
            default:
                log.warn("Unknown mail operation: {}", mailboxOperation);
                return ResponseMessage.UNKNOWN_REQUEST.getResponse();
        }
    }


    private String getReadResponse(String boxType, UserManager userManager) {
        log.info("Reading mails from box: {}", boxType);
        List<Mail> mailsToRead = mailService.getMails(boxType, userManager);

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
            mailService.markAsRead(userManager);
        }

        return response.toString();
    }

    private String getDeleteMailsResponse(String boxType, UserManager userManager){
        log.info("Deleting mails from box: {}", boxType);
        mailService.deleteMails(boxType, userManager);
        return ResponseMessage.MAIL_DELETION_SUCCEEDED.getResponse();
    }
}