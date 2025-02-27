package unit_test.repository;

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
class MailRepositoryTest {
    MailRepository mailRepository;
    UserRepository userRepository;
    SessionManager sessionManager;
    Connection connection;

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
    @DisplayName("Should test save and retrieve mail")
    void testSaveAndRetrieveMail() {
        User sender = new User("sender", "pass", User.Role.USER);
        User recipient = new User("recipient", "pass", User.Role.USER);
        userRepository.createUser(sender);
        userRepository.createUser(recipient);

        // Set the sender as the current user and save a mail
        sessionManager.setCurrentUser(sender);
        Mail mail = new Mail(sender, recipient, "Hello", LocalDateTime.now());
        mailRepository.saveMail(mail);

        // Retrieve mail from recipient's INBOX
        sessionManager.setCurrentUser(recipient);
        List<Mail> inbox = mailRepository.findMails("INBOX", sessionManager);
        assertNotNull(inbox);
        assertFalse(inbox.isEmpty());
        assertEquals("Hello", inbox.get(0).getMessage());

        // Retrieve mail from sender's SENT
        sessionManager.setCurrentUser(sender);
        List<Mail> sent = mailRepository.findMails("SENT", sessionManager);
        assertNotNull(sent);
        assertFalse(sent.isEmpty());
        assertEquals("Hello", sent.get(0).getMessage());
    }

    @Test
    @DisplayName("Should test delete mails from mailbox")
    void testDeleteMails() {
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
        assertTrue(inbox.isEmpty());

        // Sender deletes mails from SENT
        sessionManager.setCurrentUser(sender);
        mailRepository.deleteMails("SENT", sessionManager);
        List<Mail> sent = mailRepository.findMails("SENT", sessionManager);
        assertTrue(sent.isEmpty());
    }

    @Test
    @DisplayName("Should test check if mailbox is full")
    void testIsMailboxFull() {
        User sender = new User("sender3", "pass", User.Role.USER);
        User recipient = new User("recipient3", "pass", User.Role.USER);
        userRepository.createUser(sender);
        userRepository.createUser(recipient);

        // Sender sends 6 mails to recipient, exceeding the limit of 5
        sessionManager.setCurrentUser(sender);
        for (int i = 0; i < 6; i++) {
            Mail mail = new Mail(sender, recipient, "Message " + i, LocalDateTime.now());
            mailRepository.saveMail(mail);
        }

        boolean full = mailRepository.isMailboxFull(recipient);
        assertTrue(full);
    }
}
