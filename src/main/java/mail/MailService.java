package mail;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.UserManager;
import java.util.List;

 /*
  * The MailService class provides various operations related to email management,
  * including sending mail, returning mail lists, emptying mailboxes, and marking mails as read
  */

@Log4j2
public class MailService {
    public void sendMail(Mail mail) {
        mail.getRecipient().getMailBox().getUnreadBox().add(mail);
        mail.getSender().getMailBox().getSentBox().add(mail);
        log.info("Mail successfully sent to {}", mail.getRecipient().getUsername());
    }

    // Returns a list of mails to read based on the requested mail list type (e.g. OPENED, UNREAD, SENT)
    public List<Mail> getMailsToRead(String boxType) {
        log.info("Getting mails to read for mailbox: {}", boxType);
        List<Mail> mailsToRead = getMailListByType(boxType);
        if (mailsToRead != null) {
            log.info("{} mails returned for mailbox {}", boxType, UserManager.currentLoggedInUser.getUsername());
        } else {
            log.warn("Invalid mailbox type requested: {}", boxType);
        }
        return mailsToRead;
    }

    public void deleteEmails(String boxType) {
        log.info("Deleting mails from box: {}", boxType);
        List<Mail> mailList = getMailListByType(boxType);
        if (mailList != null) {
            mailList.clear();
            log.info("{} mails deleted successfully for user {}", boxType, UserManager.currentLoggedInUser.getUsername());
        } else {
            log.warn("Attempted to empty non-existent mailbox type: {}", boxType);
        }
    }

    // Returns the list of mails based on the specified type
    private List<Mail> getMailListByType(String boxType) {
        MailBox mailBox = UserManager.currentLoggedInUser.getMailBox();
        switch (boxType.toUpperCase()) {
            case "OPENED":
                return mailBox.getOpenedBox();
            case "UNREAD":
                return mailBox.getUnreadBox();
            case "SENT":
                return mailBox.getSentBox();
            default:
                log.error("Unknown mailbox type requested: {}", boxType);
                return null;
        }
    }

    // Marks all unread mails as read by moving them to the OPENED mail list and clearing the UNREAD list
    public void markMailsAsRead(String boxType){
        log.info("Marking mails as read for mailbox: {}", boxType);
        if(!boxType.equals("SENT")){
            List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadBox();
            for(Mail mail: unreadMails){
                UserManager.currentLoggedInUser.getMailBox().getOpenedBox().add(mail);
            }
            UserManager.currentLoggedInUser.getMailBox().getUnreadBox().clear();
            log.info("Marked all unread mails as read for user {}", UserManager.currentLoggedInUser.getUsername());
        } else {
        log.warn("Attempted to mark 'sent' mails as read, operation not allowed");
        }
    }
}
