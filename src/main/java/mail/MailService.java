package mail;

import lombok.extern.log4j.Log4j2;
import org.jooq.Record;
import org.jooq.Condition;
import shared.ResponseMessage;
import user.User;
import user.UserManager;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Log4j2
public class MailService {
    private final String MAILS_TABLE = "mails";

    public void sendMail(User recipient, String message, UserManager userManager) {
        log.info("Mail sending to {} from {}", recipient, UserManager.currentLoggedInUser);

        Mail mailToSender = new Mail(UserManager.currentLoggedInUser, recipient, message, Mail.Status.SENT);
        saveMail(mailToSender, userManager);

        Mail mailToRecipient = new Mail(mailToSender.getSender(), recipient, message, Mail.Status.UNREAD);
        saveMail(mailToRecipient, userManager);

        log.info("Mail successfully sent to {}", recipient.getUsername());
    }

    public void saveMail(Mail mail, UserManager userManager) {
        userManager.CREATE.insertInto(table(MAILS_TABLE),
                        field("sender_name"),
                        field("recipient_name"),
                        field("message"),
                        field("status"))
                .values(mail.getSender().getUsername(),
                        mail.getRecipient().getUsername(),
                        mail.getMessage(),
                        mail.getStatus().toString())
                .execute();
    }

    /*
     * Retrieves the list of mails
     * Handles the different mailbox types: OPENED, UNREAD, SENT.
     */
    public List<Mail> getMails(String boxType, UserManager userManager) {
        log.info("Entering getMails method with boxType: {}", boxType);

        List<Record> records = userManager.CREATE.selectFrom(MAILS_TABLE)
                .where(getMailboxCondition(boxType))
                .fetch();

        List<Mail> mails = new ArrayList<>();
        for (Record record : records) {
            //
            Mail mail = convertRecordToMail(record, userManager);
            mails.add(mail);
        }

        log.info("Exiting getMails method");
        return mails;
    }

    public Mail convertRecordToMail(Record record, UserManager userManager) {
            String message = record.getValue("message", String.class);
            String senderUsername = record.getValue("sender_name", String.class);
            String recipientUsername = record.getValue("recipient_name", String.class);
            Mail.Status status = Mail.Status.valueOf(record.getValue("status", String.class));

            User sender = userManager.getUserByUsername(senderUsername);
            User recipient = userManager.getUserByUsername(recipientUsername);

            return new Mail(sender, recipient, message, status);
    }

    public boolean isMailboxFull(User recipient, UserManager userManager){
        String unread = Mail.Status.UNREAD.toString();

        int messageCount = userManager.CREATE.selectFrom(table(MAILS_TABLE))
                .where(field("recipient_name").eq(recipient.getUsername())
                        .and(field("status").eq(unread)))
                .fetch()
                .size();

        return messageCount > 5;
    }

    public Condition getMailboxCondition(String boxType) {
        String username = UserManager.currentLoggedInUser.getUsername();
        Condition condition;

        if (boxType.equals("SENT")) {
            condition = field("sender_name").eq(username)
                    .and(field("status").eq(boxType));
        } else {
            condition = field("recipient_name").eq(username)
                    .and(field("status").eq(boxType));
        }

        return condition;
    }


    public void deleteMails(String boxType, UserManager userManager) {
        log.info("Deleting mails from box: {}", boxType);

        userManager.CREATE.deleteFrom(table(MAILS_TABLE))
                    .where(getMailboxCondition(boxType))
                    .execute();

        log.info("{} mails deleted for user {}", boxType, UserManager.currentLoggedInUser.getUsername());
    }


    public void markAsRead(UserManager userManager) {
        log.info("Marking mails as read");

        userManager.CREATE.update(table(MAILS_TABLE))
                .set(field("status"), Mail.Status.OPENED.toString())
                .where(field("recipient_name").eq(UserManager.currentLoggedInUser.getUsername()))
                .and(field("status").eq(Mail.Status.UNREAD.toString()))
                .execute();

        log.info("Marked all unread mails as opened for user {}", UserManager.currentLoggedInUser.getUsername());
    }
}