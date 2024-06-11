package handler.mail;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

import java.io.IOException;
@Log4j2
public class WriteHandler {
    private MailService mailService = new MailService();

    public String getResponse(String username, String message, UserManager userManager) throws IOException {
        log.info("Attempting to send mail to user: {}", username);
        User recipient = userManager.getUserByUsername(username);

        if (recipient != null) {
            log.warn("Mail sending failed, recipient not found: {}", username);
            return ResponseMessage.SENDING_FAILED_RECIPIENT_NOT_FOUND.getResponse();
        }

        if(recipient.getMailBox().ifUnreadBoxFull()) {
            log.warn("Mail sending failed, recipient's {} mailbox is full: ", username);
            return ResponseMessage.SENDING_FAILED_BOX_FULL.getResponse();
        }

        if(message.length() >= 255) {
            log.warn("Mail sending failed, message too long for recipient: {}", username);
            return ResponseMessage.SENDING_FAILED_TO_LONG_MESSAGE.getResponse();
        }

        mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipient, message));
        log.info("Mail sent successfully to: {}", username);
        return ResponseMessage.SENDING_SUCCEEDED.getResponse();
    }
}
