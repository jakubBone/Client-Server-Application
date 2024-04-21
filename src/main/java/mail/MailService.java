package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.UserManager;

import java.util.List;

 /*
  * The MailService class provides various operations related to email management,
  * including sending mail, returning mail lists, emptying mailboxes, and marking mails as read
  */
public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) {
        mail.getRecipient().getMailBox().getUnreadMails().add(mail);
        mail.getSender().getMailBox().getSentMails().add(mail);
        logger.info("Mail successfully sent to {}", mail.getRecipient().getUsername());
    }

    // Returns a list of mails to read based on the requested mail list type (e.g. OPENED, UNREAD, SENT)
    public List<Mail> getMailsToRead(String requestedMailList) {
        List<Mail> mailsToRead = getMailListByType(requestedMailList);
        if (mailsToRead != null) {
            logger.info("{} mails returned for user {}", requestedMailList, UserManager.currentLoggedInUser.getUsername());
        } else {
            logger.warn("Invalid mail list type requested: {}", requestedMailList);
        }
        return mailsToRead;
    }

    public void emptyMailbox(String requestedMailList) {
        List<Mail> mailList = getMailListByType(requestedMailList);
        if (mailList != null) {
            mailList.clear();
            logger.info("{} mails deleted successfully for user {}", requestedMailList, UserManager.currentLoggedInUser.getUsername());
        } else {
            logger.warn("Attempted to empty non-existent mail list type: {}", requestedMailList);
        }
    }

    // Returns the list of mails based on the specified type
    private List<Mail> getMailListByType(String type) {
        MailBox mailBox = UserManager.currentLoggedInUser.getMailBox();
        switch (type.toUpperCase()) {
            case "OPENED":
                return mailBox.getOpenedMails();
            case "UNREAD":
                return mailBox.getUnreadMails();
            case "SENT":
                return mailBox.getSentMails();
            default:
                logger.error("Unknown mail list type requested: {}", type);
                return null;
        }
    }

    // Marks all unread mails as read by moving them to the OPENED mail list and clearing the UNREAD list
    public void markMailsAsRead(String boxType){
        if(!boxType.equals("SENT")){
            List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
            for(Mail mail: unreadMails){
                UserManager.currentLoggedInUser.getMailBox().getOpenedMails().add(mail);
            }
            UserManager.currentLoggedInUser.getMailBox().getUnreadMails().clear();
            logger.info("Marked all unread mails as read for user {}", UserManager.currentLoggedInUser.getUsername());
        } else {
        logger.warn("Attempted to mark 'sent' mails as read, operation not allowed");
        }
    }
}
