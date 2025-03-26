package com.jakub.bone.repository;

import com.jakub.bone.data.DataSource;
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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.INTEGER;

@Log4j2
public class MailRepository {
    private final DSLContext context;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MailRepository(UserRepository userRepository) {
        this.context = DSL.using(DataSource.getInstance().getConnection());
        this.userRepository = userRepository;
        createTable();
    }

    public void createTable(){
        try {
            context.createTableIfNotExists("mail")
                    .column("id", INTEGER.identity(true))
                    .column("sender", VARCHAR(255).nullable(false))
                    .column("recipient", VARCHAR(255).nullable(false))
                    .column("message", VARCHAR(255).nullable(false))
                    .column("send_time", VARCHAR(255).nullable(false))
                    .column("deleted_by_sender", INTEGER.nullable(false).defaultValue(0)) // default false
                    .column("deleted_by_receiver", INTEGER.nullable(false).defaultValue(0))
                    .constraints(
                            DSL.constraint("PK_MAIL").primaryKey("id")
                    )
                    .execute();

            // Full-text search
            context.execute("ALTER TABLE mail ADD COLUMN IF NOT EXISTS message_tsv tsvector");
            context.execute("UPDATE mail SET message_tsv = to_tsvector('simple', message)");
            context.execute("CREATE INDEX IF NOT EXISTS idx_message_tsv ON mail USING gin(message_tsv)");

            // Create a trigger parsing incoming messages to tokens in message_tsv column
            context.execute(
                    "CREATE OR REPLACE FUNCTION update_message_tsv() RETURNS trigger AS $$ " +
                            "BEGIN " +
                            "  NEW.message_tsv := to_tsvector('simple', NEW.message); " +
                            "  RETURN NEW; " +
                            "END; " +
                            "$$ LANGUAGE plpgsql"
            );

            // Remove existing trigger and create a new
            context.execute("DROP TRIGGER IF EXISTS message_tsv_trigger ON mail");
            context.execute(
                    "CREATE TRIGGER message_tsv_trigger " +
                            "BEFORE INSERT OR UPDATE ON mail " +
                            "FOR EACH ROW EXECUTE PROCEDURE update_message_tsv()"
            );
        } catch (Exception e) {
            log.error("Error while 'mail' table creating: {}", e.getMessage());
            throw new RuntimeException("Failed to create 'mail' table ", e);
        }
    }

    public void truncateTable(){
        try {
            context.truncate("mail").restartIdentity().execute();
        } catch (Exception e) {
            log.error("Error while table truncating: {}", e.getMessage());
            throw new RuntimeException("Failed to truncate 'mail' table ", e);
        }
    }

    public void saveMail(Mail mail) {
        try {
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
                            0, 0)  // set false
                    .execute();
        } catch (Exception e) {
            log.error("Error while creating mail from {}: {}", mail.getSender().getUsername(), e.getMessage());
            throw new RuntimeException("Failed to create mail from" + mail.getSender(), e);
        }
    }

    /*
    *  e_commerce=# SELECT
                  product_id,
                  product_name,
                  retail_price
              FROM products
              WHERE product_name_tokens @@ to_tsquery('RIBENA');
    *
    * */

    public List<Mail> findMails(String boxType, SessionManager sessionManager) {
        try {
            String username = sessionManager.getCurrentUser().getUsername();

            Condition boxCondition = boxType.equalsIgnoreCase("SENT")
                    ? field("sender").eq(username).and(field("deleted_by_sender").eq(0))
                    : field("recipient").eq(username).and(field("deleted_by_receiver").eq(0));

            List<Record> records = context.selectFrom("mail")
                    .where(boxCondition)
                    .orderBy(field("send_time").desc())
                    .fetch();

            /* if (boxType.equalsIgnoreCase("SENT")) {
                condition = field("sender").eq(username)
                        .and(field("deleted_by_sender").eq(0)); // Only undeleted by sender
            } else { // INBOX
                condition = field("recipient").eq(username)
                        .and(field("deleted_by_receiver").eq(0)); // Only undeleted by receiver
            }
            List<Record> records = context.selectFrom("mail")
                    .where(condition)
                    .orderBy(field("send_time").desc())
                    .fetch();*/

            List<Mail> mails = new ArrayList<>();
            for (Record record : records) {
                Mail mail = mapRecordToMail(record);
                mails.add(mail);
            }
            return mails;
        } catch (Exception e) {
            log.error("Error while retrieving {} emails: {}", boxType, e.getMessage());
            throw new RuntimeException("Failed to retrieve mails: " + boxType , e);
        }
    }

    public void deleteMails(String boxType, SessionManager sessionManager) {
        try {
            String username = sessionManager.getCurrentUser().getUsername();
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
        } catch (Exception e) {
            log.error("Error while deleting {} mails: {}", boxType, e.getMessage());
            throw new RuntimeException("Failed to delete mails: " + boxType, e);
        }
    }

    public List<Mail> searchText(String boxType, SessionManager sessionManager, String query) {
        try {
            String username = sessionManager.getCurrentUser().getUsername();

            Condition boxCondition = boxType.equalsIgnoreCase("SENT")
                    ? field("sender").eq(username).and(field("deleted_by_sender").eq(0))
                    : field("recipient").eq(username).and(field("deleted_by_receiver").eq(0));

            // Full-text searching
            // 'plainto_tsquery' → simple parser)
            Condition fullTextCondition =
                    condition("message_tsv @@ plainto_tsquery('simple', ?)", query);

            List<Record> records = context
                    .selectFrom(table("mail"))
                    .where(boxCondition)
                    .and(fullTextCondition)
                    .orderBy(field("send_time").desc())
                    .fetch();

            List<Mail> result = new ArrayList<>();
            for (Record r : records) {
                result.add(mapRecordToMail(r));
            }
            return result;
        }
        catch (Exception e) {
            log.error("Error while searching mails: {}", e.getMessage());
            throw new RuntimeException("Failed to search mails", e);
        }
    }

    public Mail mapRecordToMail(Record record) {
        try {
            String message = record.getValue("message", String.class);
            String senderUsername = record.getValue("sender", String.class);
            String recipientUsername = record.getValue("recipient", String.class);
            String sendTimeStr = record.getValue("send_time", String.class);

            LocalDateTime sendTime = LocalDateTime.parse(sendTimeStr, formatter);

            User sender = userRepository.findUserByUsername(senderUsername);
            User recipient = userRepository.findUserByUsername(recipientUsername);

            return new Mail(sender, recipient, message, sendTime);
        } catch (Exception e) {
            log.error("Error while mapping record to mail {}", e.getMessage());
            throw new RuntimeException("Failed to map mail", e);
        }
    }

    public boolean isMailboxFull(User recipient) {
        try {
            int messageCount = context.selectFrom(table("mail"))
                    .where(field("recipient").eq(recipient.getUsername()))
                    .fetch()
                    .size();
            return messageCount > 5;
        } catch (Exception e) {
            log.error("Error while checking mailbox capacity for user {}: {}", recipient.getUsername(), e.getMessage());
            throw new RuntimeException("Failed to check mailbox capacity for user" + recipient.getUsername(), e);
        }

    }
}
