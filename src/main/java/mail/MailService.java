package mail;

import database.DatabaseConnection;
import database.MailDAO;
import database.UserDAO;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import user.User;
import user.UserManager;

import java.util.List;

@Log4j2
public class MailService {
    private final DSLContext create;
    private final MailDAO mailDAO;
    private UserDAO userDAO;
    public MailService() {
        this.create = DSL.using(DatabaseConnection.getInstance().getConnection());
        this.userDAO = new UserDAO(create);
        this.mailDAO = new MailDAO(create, userDAO);
    }

    public void sendMail(User recipient, String message) {
        log.info("Mail sending to {} from {}", recipient, UserManager.currentLoggedInUser);

        Mail mailToSender = new Mail(UserManager.currentLoggedInUser, recipient, message, Mail.Status.SENT);
        mailDAO.saveMailToDB(mailToSender);

        Mail mailToRecipient = new Mail(mailToSender.getSender(), recipient, message, Mail.Status.UNREAD);
        mailDAO.saveMailToDB(mailToRecipient);

        log.info("Mail successfully sent to {}", recipient.getUsername());
    }

    public List<Mail> getMails(String boxType) {
        log.info("Entering getMails method with boxType: {}", boxType);
        return mailDAO.getMailsFromDB(boxType);
    }

    public boolean isMailboxFull(User recipient){
        return mailDAO.isMailboxFullInDB(recipient);
    }


    public void deleteMails(String boxType) {
        log.info("Deleting mails from box: {}", boxType);

        mailDAO.deleteMailsFromDB(boxType);

        log.info("{} mails deleted for user {}", boxType, UserManager.currentLoggedInUser.getUsername());
    }

    public void markAsRead() {
        log.info("Marking mails as read");

        mailDAO.markAsReadInDB();

        log.info("Marked all unread mails as opened for user {}", UserManager.currentLoggedInUser.getUsername());
    }
}