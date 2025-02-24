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
import java.time.format.DateTimeFormatter;
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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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
                .column("send_time", VARCHAR(255).nullable(false))
                .column("deleted_by_sender", INTEGER.nullable(false).defaultValue(0)) // 0 = false
                .column("deleted_by_receiver", INTEGER.nullable(false).defaultValue(0))
                .constraints(
                        DSL.constraint("PK_MAIL").primaryKey("id")
                )
                .execute();
    }


    public void clearTable(){
        context.truncate("mail").restartIdentity().execute();
    }

    public void createMail(Mail mail) {
        // Date format: yyyy-MM-dd HH:mm:ss
        String formattedDate = mail.getSendTime().format(formatter);
        context.insertInto(table("mail"),
                        field("sender"),
                        field("recipient"),
                        field("message"),
                        field("send_time"),
                        field("deleted_by_sender"),
                        field("deleted_by_receiver"))
                .values(mail.getSender().getUsername(),
                        mail.getRecipient().getUsername(),
                        mail.getMessage(),
                        formattedDate,
                        0, 0)  // deletion set false
                .execute();
    }

    public List<Mail> findMails(String boxType) {
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        Condition condition;
        if (boxType.equalsIgnoreCase("SENT")) {
            condition = field("sender").eq(username)
                    .and(field("deleted_by_sender").eq(0)); // Only undeleted by sender
        } else { // INBOX
            condition = field("recipient").eq(username)
                    .and(field("deleted_by_receiver").eq(0)); // Only undeleted by receiver
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
            context.update(table("mail"))
                    .set(field("deleted_by_sender"), 1)
                    .where(field("sender").eq(username))
                    .execute();
        } else { // INBOX
            context.update(table("mail"))
                    .set(field("deleted_by_receiver"), 1)
                    .where(field("recipient").eq(username))
                    .execute();
        }
    }

    public Mail mapRecordToMail(Record record) {
        String message = record.getValue("message", String.class);
        String senderUsername = record.getValue("sender", String.class);
        String recipientUsername = record.getValue("recipient", String.class);
        String sendTimeStr = record.getValue("send_time", String.class);

        LocalDateTime sendTime = LocalDateTime.parse(sendTimeStr, formatter);

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
