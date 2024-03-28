package mail;

import exceptions.MailboxOverflowException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.User;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);
    public void sendMessage(User receiver, Mail mail) throws MailboxOverflowException {
        if(receiver.getMailBox().ifBoxFull())
            throw new MailboxOverflowException("Unable to send mail");
        else {
            if(mail.getMaxMessageLength() <= 255) {
                receiver.getMailBox().receive(mail);
            }
        }
    }

    public void readMessages(User user){
        for (Mail mail: user.getMailBox().getMailList()){
            System.out.println(mail.getMessage());
            mail.markAsRead();
            logger.info("Mail opened");
        }
    }

    public void deleteMessage(User user, Mail mail){
        user.getMailBox().delete(mail);
    }
}
