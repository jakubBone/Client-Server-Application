package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.UserManager;

import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) /*throws MailboxOverflowException */{
        if(mail.getRecipient().getMailBox().ifBoxFull()){
            logger.info("Mailboxfull");
            /*throw new MailboxOverflowException("Receiver mailbox is full. Unable to send mail");*/
        } else {
            if(mail.getMessageLength() <= 255) {
                System.out.println("Current User: " + UserManager.currentLoggedInUser);
                System.out.println("Recepient: " + mail.getRecipient());
                System.out.println("Message: " + mail.getMessage());
                System.out.println("Mail length: " + mail.getMessageLength());
                mail.getRecipient().getMailBox().getUnreadMails().add(mail);
                logger.info("Mail sent to receiver");
            }
        }
    }

    public List <Mail> getMailsToRead(String requestedMailList) {
        List<Mail> mailsToRead = null;
        switch (requestedMailList){
            case "OPENED":
                logger.info("Opened mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getOpenedMails();
                break;
            case "UNREAD":
                logger.info("Unread mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
                break;
        }
        return mailsToRead;
    }

    public void deleteMail(Mail mail){
        mail.getSender().getMailBox().getOpenedMails().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
