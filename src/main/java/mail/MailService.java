package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.UserManager;
import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) {
        mail.getRecipient().getMailBox().getUnreadMails().add(mail);
        mail.getSender().getMailBox().getSentMails().add(mail);
        logger.info("Mail sent to receiver");
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
            case "SENT":
                logger.info("Sent mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getSentMails();
                break;
            default:
                logger.info("Incorrect");
        }
        return mailsToRead;
    }

    public void markMailsAsRead(String boxType){
        if(!boxType.equals("SENT")){
            List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
            for(Mail mail: unreadMails){
                UserManager.currentLoggedInUser.getMailBox().getOpenedMails().add(mail);
            }
            UserManager.currentLoggedInUser.getMailBox().getUnreadMails().clear();
        }
    }

    public void emptyMailbox(String boxType){
        logger.info("Mails deleted successfully");
    }
}
