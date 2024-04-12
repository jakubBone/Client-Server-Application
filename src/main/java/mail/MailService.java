package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.UserManager;
import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) {
            if(mail.getMessageLength() <= 255) {
                mail.getRecipient().getMailBox().getUnreadMails().add(mail);
                logger.info("Mail sent to receiver");
            } else{
                System.out.println("Message too long");
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

    public void markMailsAsRead(){
        List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
        for(Mail mail: unreadMails){
            UserManager.currentLoggedInUser.getMailBox().getOpenedMails().add(mail);
        }
        UserManager.currentLoggedInUser.getMailBox().getUnreadMails().clear();
    }

    public void deleteMail(Mail mail){
        mail.getSender().getMailBox().getOpenedMails().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
