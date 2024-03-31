package mail;

import exceptions.MailboxOverflowException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.User;

import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) throws MailboxOverflowException {
        if(mail.getReceiver().getMailBox().ifBoxFull())
            throw new MailboxOverflowException("Receiver mailbox is full. Unable to send mail");
        else {
            if(mail.getMaxMessageLength() <= 255) {
                mail.getReceiver().getMailBox().getUnreadMails().add(mail);
                logger.info("Mail sent to receiver");
            }
        }
    }

    public void readMail(User user){
        List<Mail> mailsToRead = user.getMailBox().getReadMails();
        if(mailsToRead.isEmpty()){
            throw new MailboxOverflowException("There is no unread mails in mailbox")
        }
        for (Mail mail: mailsToRead){
            System.out.println(mail.getMessage());
            mail.markAsRead();
            logger.info("Mail read");
        }
    }

    public void deleteMail(Mail mail){
        mail.getSender().getMailBox().getReadMails().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
