package com.jakub.bone.repository;

import com.jakub.bone.domain.Mail;
import com.jakub.bone.session.SessionManager;
import lombok.extern.log4j.Log4j2;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.User;

import java.time.LocalDateTime;
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
                .column("send_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false))
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
                        field("send_time"))
                .values(mail.getSender().getUsername(),
                        mail.getRecipient().getUsername(),
                        mail.getMessage(),
                        mail.getSendTime().toString())
                .execute();
    }

    public List<Mail> findMails(String boxType) {
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        Condition condition;
        if (boxType.equalsIgnoreCase("SENT")) {
            condition = field("sender").eq(username);
        } else { // INBOX
            condition = field("recipient").eq(username);
        }
        List<Record> records = context.selectFrom("mail")
                .where(condition)
                .orderBy(field("send_time").desc())
                .fetch();

        List<Mail> mails = new ArrayList<>();
        for (Record record : records) {
            Mail mail = mapRecordToMail(record);
            mails.add(mail);
        }
        return mails;
    }

    public void deleteMails(String boxType) {
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        if (boxType.equalsIgnoreCase("SENT")) {
            context.deleteFrom(table("mail"))
                    .where(field("sender").eq(username))
                    .execute();
        } else { // INBOX
            context.deleteFrom(table("mail"))
                    .where(field("recipient").eq(username))
                    .execute();
        }
    }

    public Mail mapRecordToMail(Record record) {
        String message = record.getValue("message", String.class);
        String senderUsername = record.getValue("sender", String.class);
        String recipientUsername = record.getValue("recipient", String.class);
        String sendTimeStr = record.getValue("send_time", String.class);
        LocalDateTime sendTime = LocalDateTime.parse(sendTimeStr);

        User sender = userRepository.findUserByUsername(senderUsername);
        User recipient = userRepository.findUserByUsername(recipientUsername);

        return new Mail(sender, recipient, message, sendTime);
    }

    public boolean isMailboxFull(User recipient) {
        int messageCount = context.selectFrom(table("mail"))
                .where(field("recipient").eq(recipient.getUsername()))
                .fetch()
                .size();

        return messageCount > 5;
    }
}
