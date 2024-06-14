package mail;

import lombok.extern.log4j.Log4j2;
import user.UserManager;
import java.util.List;

@Log4j2
public class MailService {
    public void sendMail(Mail mail) {
        mail.getRecipient().getMailBox().getUnreadBox().add(mail);
        mail.getSender().getMailBox().getSentBox().add(mail);
        log.info("Mail successfully sent to {}", mail.getRecipient().getUsername());
    }

    /*
     * Retrieves a list of mails based on the specified mailbox type (e.g., OPENED, UNREAD, SENT).
     * Logs the retrieval operation and handles invalid mailbox types.
     */
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

    /*
     * Deletes all emails from the specified mailbox type.
     * Logs the deletion operation and handles invalid mailbox types.
     */
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

    /*
     * Retrieves the list of mails
     * Handles the different mailbox types: OPENED, UNREAD, SENT.
     */
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

    /*
     * Marks all unread mails as read by moving them to the opened box and clearing the unread box.
     * Logs the operation and ensures the 'sent' mails are not marked as read.
     */
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