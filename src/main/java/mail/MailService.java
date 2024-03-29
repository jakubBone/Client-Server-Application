package mail;

import exceptions.MailboxOverflowException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.User;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(User sender, User receiver, Mail mail) throws MailboxOverflowException {
        if(receiver.getMailBox().ifBoxFull())
            throw new MailboxOverflowException("Unable to send mail");
        else {
            if(mail.getMaxMessageLength() <= 255) {
                receiver.getMailBox().getMailList().add(mail);
            }
        }
    }

    public void readMail(User user){
        for (Mail mail: user.getMailBox().getMailList()){
            System.out.println(mail.getMessage());
            mail.markAsRead();
            logger.info("Mail opened");
        }
    }

    public void receiveMail(User user, Mail mail) throws MailboxOverflowException{
        if(user.getMailBox().ifBoxFull()){
            throw new MailboxOverflowException("Unable to receive mail");
        } else {
            user.getMailBox().getMailList().add(mail);
            logger.info("New unread mail");
        }
    }

    public void deleteMail(User user, Mail mail){
        user.getMailBox().getMailList().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
