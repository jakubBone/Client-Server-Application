package handler.mail;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

/*
 * The WriteHandler class processes requests to send mail.
 * It interacts with the MailService and UserManager to send emails and handle related validations.
 */

@Log4j2
public class WriteHandler {
    private MailService mailService = new MailService();

    public String getResponse(String username, String message, UserManager userManager) throws IOException {
        log.info("Attempting to send mail to user: {}", username);
        User recipient = userManager.getUserByUsername(username);

        if (recipient == null) {
            log.warn("Mail sending failed, recipient not found: {}", username);
            return ResponseMessage.SENDING_FAILED_RECIPIENT_NOT_FOUND.getResponse();
        }

        if(recipient.getUnreadMails().size() >= 5) {
            log.warn("Mail sending failed, recipient's {} mailbox is full: ", username);
            return ResponseMessage.SENDING_FAILED_BOX_FULL.getResponse();
        }

        if(message.length() >= 255) {
            log.warn("Mail sending failed, message too long for recipient: {}", username);
            return ResponseMessage.SENDING_FAILED_TO_LONG_MESSAGE.getResponse();
        }

        Mail newMail = new  Mail(UserManager.currentLoggedInUser, recipient, message);
        mailService.sendMail(newMail);

        log.info("Mail sent successfully to: {}", username);
        return ResponseMessage.SENDING_SUCCEEDED.getResponse();
    }
}
