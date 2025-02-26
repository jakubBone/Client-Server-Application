package repository;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.domain.User;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.session.SessionManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class MailRepositoryTest {
    private MailRepository mailRepository;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private Connection connection;

    @BeforeAll
    void setUp() {
        connection = DataSource.getInstance().getConnection();
        userRepository = new UserRepository();
        mailRepository = new MailRepository(userRepository);
        sessionManager = new SessionManager();
        mailRepository.createTable();
    }

    @AfterEach
    void cleanUp() {
        mailRepository.truncateTable();
        userRepository.truncateTable();
    }

    @Test
    @DisplayName("Save and retrieve mail")
    void testSaveAndRetrieveMail() {
        // Create sender and recipient users
        User sender = new User("sender", "pass", User.Role.USER);
        User recipient = new User("recipient", "pass", User.Role.USER);
        userRepository.createUser(sender);
        userRepository.createUser(recipient);

        // Set the sender as the current user and save a mail
        sessionManager.setCurrentUser(sender);
        Mail mail = new Mail(sender, recipient, "Hello!", LocalDateTime.now());
        mailRepository.saveMail(mail);

        // Retrieve mail from recipient's inbox
        sessionManager.setCurrentUser(recipient);
        List<Mail> inbox = mailRepository.findMails("INBOX", sessionManager);
        assertNotNull(inbox, "Inbox should not be null");
        assertFalse(inbox.isEmpty(), "Inbox should not be empty");
        assertEquals("Hello!", inbox.get(0).getMessage(), "Mail message should match");

        // Retrieve mail from sender's sent mailbox
        sessionManager.setCurrentUser(sender);
        List<Mail> sent = mailRepository.findMails("SENT", sessionManager);
        assertNotNull(sent, "Sent mailbox should not be null");
        assertFalse(sent.isEmpty(), "Sent mailbox should not be empty");
        assertEquals("Hello!", sent.get(0).getMessage(), "Mail message should match");
    }

    @Test
    @DisplayName("Delete mails from mailbox")
    void testDeleteMails() {
        // Create sender and recipient users
        User sender = new User("sender2", "pass", User.Role.USER);
        User recipient = new User("recipient2", "pass", User.Role.USER);
        userRepository.createUser(sender);
        userRepository.createUser(recipient);

        // Sender sends a mail to recipient
        sessionManager.setCurrentUser(sender);
        Mail mail = new Mail(sender, recipient, "Test deletion", LocalDateTime.now());
        mailRepository.saveMail(mail);

        // Recipient deletes mails from INBOX
        sessionManager.setCurrentUser(recipient);
        mailRepository.deleteMails("INBOX", sessionManager);
        List<Mail> inbox = mailRepository.findMails("INBOX", sessionManager);
        assertTrue(inbox.isEmpty(), "Inbox should be empty after deletion");

        // Sender deletes mails from SENT mailbox
        sessionManager.setCurrentUser(sender);
        mailRepository.deleteMails("SENT", sessionManager);
        List<Mail> sent = mailRepository.findMails("SENT", sessionManager);
        assertTrue(sent.isEmpty(), "Sent mailbox should be empty after deletion");
    }
}
