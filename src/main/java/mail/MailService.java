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

    public List<Mail> getMailsToRead(String requestedMailList) {
        List<Mail> mailsToRead = getMailListByType(requestedMailList);
        logger.info(requestedMailList + " mails returned");
        return mailsToRead;
    }

    public void emptyMailbox(String requestedMailList) {
        List<Mail> mailList = getMailListByType(requestedMailList);
        mailList.clear();
        logger.info(requestedMailList + " mails deleted successfully");
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
        }
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
}
