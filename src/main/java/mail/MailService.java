package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.User;
import user.UserManager;

import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) /*throws MailboxOverflowException*/ {
        if(mail.getRecipient().getMailBox().ifBoxFull()){
            //throw new MailboxOverflowException("Receiver mailbox is full. Unable to send mail");
        } else {
            if(mail.getMaxMessageLength() <= 255) {
                mail.getRecipient().getMailBox().getUnreadMails().add(mail);
                logger.info("Mail sent to receiver");
            }
        }
    }

    public List <Mail> getMailsToRead(String requestedMailList) {
        List<Mail> mailsToRead = null;
        if(requestedMailList.equals("OPENED")){
            logger.info("Opened mails returned");
            mailsToRead = UserManager.currentLoggedInUser.getMailBox().getOpenedMails();
        } else if(requestedMailList.equals("UNREAD")){
            logger.info("Unread mails returned");
            mailsToRead = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
        }
        return mailsToRead;
    }

    public void deleteMail(Mail mail){
        mail.getSender().getMailBox().getOpenedMails().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
