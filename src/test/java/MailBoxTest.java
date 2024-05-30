import mail.Mail;
import mail.MailBox;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;

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
        Assertions.assertFalse(recipient.getMailBox().ifUnreadBoxFull());
    }

    @Test
    @DisplayName("Should test if unread box is full when it contains 5 messages")
    void testIfUnreadBoxFull() {
        for (int i = 0; i < 5; i++) {
            recipient.getMailBox().getUnreadBox().add(mail);
        }
        Assertions.assertTrue(recipient.getMailBox().ifUnreadBoxFull());
    }

    @Test
    @DisplayName("Should test if unread box is full when it contains more than 5 messages")
    void testIfUnreadBoxOverFull() {
        for (int i = 0; i < 8; i++) {
            recipient.getMailBox().getUnreadBox().add(mail);
        }
        Assertions.assertTrue(recipient.getMailBox().ifUnreadBoxFull());
    }
}
