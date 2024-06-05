package operations;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
import user.User;
import user.UserManager;

import java.io.IOException;
@Log4j2
public class WriteHandler {
    private MailService mailService = new MailService();

    public String getWriteResponse(String recipient, String message, UserManager userManager) throws IOException {
        User recipientUser = userManager.getUserByUsername(recipient);
        String response = null;
        if (recipientUser != null) {
            if(recipientUser.getMailBox().ifUnreadBoxFull()){
                log.warn("Mail sending failed, recipient's mailbox is full: {}", recipient);
                response = "Sending failed: Recipient's mailbox is full";
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    log.info("Mail sent successfully to: {}", recipient);
                    response = "Mail sent successfully";
                } else {
                    log.warn("Mail sending failed, message too long for recipient: {}", recipient);
                    response = "Sending failed: Message too long (maximum 255 characters)";
                }
            }
        } else {
            log.warn("Mail sending failed, recipient not found: {}", recipient);
            response = "Sending failed: Recipient not found";
        }
        return response;
    }
}
