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
        logger.info("Mail successfully sent to {}", mail.getRecipient().getUsername());
    }

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
