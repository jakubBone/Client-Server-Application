package mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import user.UserManager;
import java.util.List;

public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class);

    public void sendMail(Mail mail) {
                mail.getRecipient().getMailBox().getUnreadMails().add(mail);
                System.out.println("UNREADS: " + mail.getRecipient().getMailBox().getUnreadMails());
                mail.getSender().getMailBox().getSentMails().add(mail);
                System.out.println("Sender: " + mail.getSender());
                System.out.println("Recipient: " + mail.getRecipient());
                logger.info("Mail sent to receiver");

    }

    public List <Mail> getMailsToRead(String requestedMailList) {
        List<Mail> mailsToRead = null;
        switch (requestedMailList){
            case "OPENED":
                logger.info("Opened mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getOpenedMails();
                System.out.println("1 " + mailsToRead);
                break;
            case "UNREAD": /// zawsze null
                logger.info("Unread mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
                System.out.println("2 " + mailsToRead);
                break;
            case "SENT":
                logger.info("Sent mails returned");
                mailsToRead = UserManager.currentLoggedInUser.getMailBox().getSentMails();
                System.out.println("3 " + mailsToRead);
                break;
            default:
                logger.info("Incorrect");
        }
        return mailsToRead;
    }

    public void markMailsAsRead(List<Mail> mailsToRead){
        List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
        if(mailsToRead.equals(unreadMails)){
            for(Mail mail: unreadMails){
                UserManager.currentLoggedInUser.getMailBox().getOpenedMails().add(mail);
            }
            UserManager.currentLoggedInUser.getMailBox().getUnreadMails().clear();
        }
    }


    /*public void markMailsAsRead(List<Mail> mailsToRead){
        List<Mail> unreadMails = UserManager.currentLoggedInUser.getMailBox().getUnreadMails();
        for(Mail mail: unreadMails){
            UserManager.currentLoggedInUser.getMailBox().getOpenedMails().add(mail);

        }
        UserManager.currentLoggedInUser.getMailBox().getUnreadMails().clear();
    }*/

    public void deleteMail(Mail mail){
        mail.getSender().getMailBox().getOpenedMails().remove(mail);
        logger.info("Mail deleted successfully");
    }
}
