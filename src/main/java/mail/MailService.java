package mail;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MailService {
    public void sendMail(Mail mail) {
        mail.getSender().addSentMail(mail);
        mail.getRecipient().addUnreadMail(mail);
        log.info("Mail successfully sent to {}", mail.getRecipient().getUsername());
    }

    /*
     * Retrieves the list of mails
     * Handles the different mailbox types: OPENED, UNREAD, SENT.
     */
    public List<Mail> getMails(String boxType) {
        log.info("Getting mails to read for mailbox: {}", boxType);
        List<Mail> mails;
        switch (boxType.toUpperCase()) {
            case "OPENED":
                mails = UserManager.currentLoggedInUser.getOpenedMails();
                break;
            case "UNREAD":
                mails = UserManager.currentLoggedInUser.getUnreadMails();
                break;
            case "SENT":
                mails = UserManager.currentLoggedInUser.getSentMails();
                break;
            default:
                log.warn("Invalid mailbox type: {}", boxType);
                return new ArrayList<>();
        }
        if (mails != null) {
            log.info("{} mails returned for mailbox {}", boxType, UserManager.currentLoggedInUser.getUsername());
        } else {
            log.warn("Mailbox empty: {}", boxType);
        }
        return mails;
    }

    /*
     * Deletes all emails from the specified mailbox type.
     * Logs the deletion operation and handles invalid mailbox types.
     */
    public void deleteEmails(String boxType) {
        log.info("Deleting mails from box: {}", boxType);
        switch (boxType.toUpperCase()) {
            case "OPENED":
                UserManager.currentLoggedInUser.getOpenedMails().clear();
                break;
            case "UNREAD":
                UserManager.currentLoggedInUser.getUnreadMails().clear();
                break;
            case "SENT":
                UserManager.currentLoggedInUser.getSentMails().clear();
                break;
            default:
                log.warn("Invalid mailbox type: {}", boxType);
        }
        log.info("{} mails deleted for user {}", boxType, UserManager.currentLoggedInUser.getUsername());
    }

    /*
     * Marks all unread mails as opened by moves it to the opened box
     */
    public void markMailAsRead(String boxType) {
        log.info("Marking mails as read for mailbox: {}", boxType);
        List<Mail> unreadMails = UserManager.currentLoggedInUser.getUnreadMails();
        for(Mail mail: unreadMails){
            UserManager.currentLoggedInUser.addOpenedMail(mail);
        }
        UserManager.currentLoggedInUser.getUnreadMails().clear();
        log.info("Marked all unread mails as opened for user {}", UserManager.currentLoggedInUser.getUsername());
    }

}