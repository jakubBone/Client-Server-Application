package com.jakub.bone.repository;

import com.jakub.bone.domain.Mail;
import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.INTEGER;

@Log4j2
public class MailRepository {
    private final DSLContext context;
    private final UserRepository userRepository;

    public MailRepository(DSLContext context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
        createTable();
    }

    public void createTable(){
        context.createTableIfNotExists("mail")
                .column("id", INTEGER.identity(true))
                .column("sender", VARCHAR(255).nullable(false))
                .column("recipient", VARCHAR(255).nullable(false))
                .column("message", VARCHAR(255).nullable(false))
                .column("status", VARCHAR(50).nullable(false))
                .constraints(
                        DSL.constraint("PK_MAIL").primaryKey("id")
                )
                .execute();
    }

    public void clearTable(){
        context.truncate("mail").restartIdentity().execute();
    }

    public void createMail(Mail mail) {
        context.insertInto(table("mail"),
                        field("sender"),
                        field("recipient"),
                        field("message"),
                        field("status"))
                .values(mail.getSender().getUsername(),
                        mail.getRecipient().getUsername(),
                        mail.getMessage(),
                        mail.getStatus().toString())
                .execute();
    }

    public List<Mail> findMails(String boxType) {
        List<Record> records = context.selectFrom("mail")
                .where(getMailboxCondition(boxType))
                .fetch();

        List<Mail> mails = new ArrayList<>();
        for (Record record : records) {
            System.out.println("in loop");
            Mail mail = mapRecordToMail(record);
            mails.add(mail);
        }
        return mails;
    }

    public void deleteMails(String boxType) {
        context.deleteFrom(table("mail"))
                .where(getMailboxCondition(boxType))
                .execute();
    }

    public Mail mapRecordToMail(Record record) {
        String message = record.getValue("message", String.class);
        String senderUsername = record.getValue("sender", String.class);
        String recipientUsername = record.getValue("recipient", String.class);
        Mail.Status status = Mail.Status.valueOf(record.getValue("status", String.class));

        User sender = userRepository.findUserByUsername(senderUsername);
        User recipient = userRepository.findUserByUsername(recipientUsername);

        return new Mail(sender, recipient, message, status);
    }

    public Condition getMailboxCondition(String boxType) {
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        Condition condition;

        if (boxType.equals(Mail.Status.SENT.toString())) {
            condition = field("sender").eq(username)
                    .and(field("status").eq(boxType));
        } else {
            condition = field("recipient").eq(username)
                    .and(field("status").eq(boxType));
        }

        return condition;
    }

    public boolean isMailboxFull(User recipient){
        String unread = Mail.Status.UNREAD.toString();

        int messageCount = context.selectFrom(table("mail"))
                .where(field("recipient").eq(recipient.getUsername())
                        .and(field("status").eq(unread)))
                .fetch()
                .size();

        return messageCount > 5;
    }

    public void markAsReadInDB() {
        context.update(table("mail"))
                .set(field("status"), Mail.Status.OPENED.toString())
                .where(field("recipient").eq(SessionManager.getInstance().getCurrentUser().getUsername()))
                .and(field("status").eq(Mail.Status.UNREAD.toString()))
                .execute();

    }
}
