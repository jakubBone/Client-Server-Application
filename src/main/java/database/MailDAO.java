package database;

import mail.Mail;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import user.User;
import user.UserManager;


import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Log4j2
public class MailDAO {
    private final DSLContext create;
    private final String MAILS_TABLE = "mails";

    public MailDAO(DSLContext create) {
        this.create = create;
    }

    public void saveMailToDB(Mail mail) {
        create.insertInto(table(MAILS_TABLE),
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

    public List<Mail> getMailsFromDB(String boxType) {
        List<Record> records = create.selectFrom(MAILS_TABLE)
                .where(getMailboxCondition(boxType))
                .fetch();

        List<Mail> mails = new ArrayList<>();
        for (Record record : records) {
            Mail mail = convertRecordToMail(record);
            mails.add(mail);
        }

        return mails;
    }

    public void deleteMailsFromDB(String boxType) {
        log.info("Deleting mails from box: {}", boxType);

        create.deleteFrom(table(MAILS_TABLE))
                .where(getMailboxCondition(boxType))
                .execute();

        log.info("{} mails deleted for user {}", boxType, UserManager.currentLoggedInUser.getUsername());
    }

    public Mail convertRecordToMail(Record record) {
        String message = record.getValue("message", String.class);
        String senderUsername = record.getValue("sender_name", String.class);
        String recipientUsername = record.getValue("recipient_name", String.class);
        Mail.Status status = Mail.Status.valueOf(record.getValue("status", String.class));

        User sender = userDAO.getUserFromDB(senderUsername);
        User recipient = userDAO.getUserFromDB(recipientUsername);

        return new Mail(sender, recipient, message, status);
    }

    public Condition getMailboxCondition(String boxType) {
        String username = UserManager.currentLoggedInUser.getUsername();
        Condition condition;

        if (boxType.equals(Mail.Status.SENT.toString())) {
            condition = field("sender_name").eq(username)
                    .and(field("status").eq(boxType));
        } else {
            condition = field("recipient_name").eq(username)
                    .and(field("status").eq(boxType));
        }

        return condition;
    }

    public boolean isMailboxFullInDB(User recipient){
        String unread = Mail.Status.UNREAD.toString();

        int messageCount = create.selectFrom(table(MAILS_TABLE))
                .where(field("recipient_name").eq(recipient.getUsername())
                        .and(field("status").eq(unread)))
                .fetch()
                .size();

        return messageCount > 5;
    }

    public void markAsReadInDB() {
        log.info("Marking mails as read");

        create.update(table(MAILS_TABLE))
                .set(field("status"), Mail.Status.OPENED.toString())
                .where(field("recipient_name").eq(UserManager.currentLoggedInUser.getUsername()))
                .and(field("status").eq(Mail.Status.UNREAD.toString()))
                .execute();

        log.info("Marked all unread mails as opened for user {}", UserManager.currentLoggedInUser.getUsername());
    }
}
