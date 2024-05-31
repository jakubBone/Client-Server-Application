import mail.Mail;
import mail.MailBox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MailBox class.
 * This class tests the operations on the mailbox, including checking if the unread box is full.
 */
public class MailBoxTest {

    private MailBox mailBox;
    private User sender;
    private User recipient;
    private Mail mail;

    @BeforeEach
    void setUp() {
        mailBox = new MailBox();
        sender = new User("exampleSender", "examplePassword", User.Role.USER);
        recipient = new User("exampleRecipient", "examplePassword", User.Role.USER);
        mail = new Mail(sender, recipient, "Example message");
    }

    @Test
    @DisplayName("Should test if unread box is not full when it contains less than 5 messages")
    void testIfUnreadBoxNotFull() {
        recipient.getMailBox().getUnreadBox().add(mail);
        // Test if the unread box is not full when it has ony 1 message
        assertFalse(recipient.getMailBox().ifUnreadBoxFull());
    }

    @Test
    @DisplayName("Should test if unread box is full when it contains 5 messages")
    void testIfUnreadBoxFull() {
        for (int i = 0; i < 5; i++) {
            recipient.getMailBox().getUnreadBox().add(mail);
        }
        // Test if the unread box is full when it has exactly 5 messages
        assertTrue(recipient.getMailBox().ifUnreadBoxFull());
    }

    @Test
    @DisplayName("Should test if unread box is full when it contains more than 5 messages")
    void testIfUnreadBoxOverFull() {
        for (int i = 0; i < 8; i++) {
            recipient.getMailBox().getUnreadBox().add(mail);
        }
        // Test if the unread box is full when it has more than 5 messages
        assertTrue(recipient.getMailBox().ifUnreadBoxFull());
    }
}
