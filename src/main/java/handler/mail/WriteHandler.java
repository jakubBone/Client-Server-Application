package handler.mail;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

/**
 * The WriteHandler class processes requests to send mail.
 * It interacts with the MailService and UserManager to send emails and handle related validations.
 */

@Log4j2
public class WriteHandler {
    private MailService mailService = new MailService();

    /**
     * Processes a request to send mail and generates an appropriate response.
     * @param recipientUsername The username of the recipient
     * @param message The message to be sent
     * @param userManager The UserManager instance for managing users
     * @return The response message as a string
     */
    public String getResponse(String recipientUsername, String message, UserManager userManager) throws IOException {
        log.info("Attempting to send mail to user: {}", recipientUsername);

        User recipient = userManager.getUserByUsername(recipientUsername);

        if (recipient == null) {
            log.warn("Mail sending failed, recipient not found: {}", recipientUsername);
            return ResponseMessage.SENDING_FAILED_RECIPIENT_NOT_FOUND.getResponse();
        }

        if(mailService.isMailboxFull(recipient, userManager)){
            log.warn("Mail sending failed, recipient's {} mailbox is full: ", recipientUsername);
            return ResponseMessage.SENDING_FAILED_BOX_FULL.getResponse();
        }

        if(message.length() >= 255) {
            log.warn("Mail sending failed, message too long for recipient: {}", recipientUsername);
            return ResponseMessage.SENDING_FAILED_TO_LONG_MESSAGE.getResponse();
        }

        mailService.sendMail(recipient, message, userManager);

        log.info("Mail sent successfully to: {}", recipientUsername);
        return ResponseMessage.SENDING_SUCCEEDED.getResponse();
    }
}
