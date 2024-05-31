import mail.Mail;
import mail.MailBox;
import mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserManager;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MailService class.
 * This class tests operations related to sending and managing emails.
 */
public class MailServiceTest {
    private User sender;
    private User recipient;
    private Mail mail;
    private MailService mailService;
    private MailBox mailBox;


    @BeforeEach
    void setUp() {
        sender = new User("senderName", "examplePassword", User.Role.USER);
        recipient = new User("recipientName", "examplePassword", User.Role.USER);
        mail = new Mail(sender, recipient, "Example message");
        mailService = new MailService();
        mailBox = new MailBox();
        UserManager.currentLoggedInUser = sender;
    }

    @Test
    @DisplayName("Should test mail sending")
    void testSendMail() {
        mailService.sendMail(mail);

        assertTrue(sender.getMailBox().getSentBox().contains(mail));
        assertTrue(recipient.getMailBox().getUnreadBox().contains(mail));
    }

    @Test
    @DisplayName("Should test specific mail list returning")
    void testGetMailsToRead() {
        String requestedMailBox = "UNREAD";

        // Test returning unread mails
        List<Mail> mailList = mailService.getMailsToRead(requestedMailBox);

        assertTrue(mailBox.getUnreadBox().equals(mailList));
    }

    @Test
    @DisplayName("Should test emptying mailbox")
    void testEmptyMailbox() {
        String requestedMailBox = "OPEN";

        // Test emptying the opened mailbox
        mailService.emptyMailbox(requestedMailBox);

        assertTrue(mailBox.getOpenedBox().isEmpty());
    }

    @Test
    @DisplayName("Should test marking mails as read")
    void testMarkMailsAsRead() {
        String requestedMailBox = "UNREAD";
        List<Mail> mailList = List.of(mail);
        mailBox.setUnreadBox(mailList);

        assertFalse(mailBox.getUnreadBox().isEmpty());
        // Test marking mails as read
        mailService.markMailsAsRead(requestedMailBox);
        assertTrue(mailBox.getSentBox().isEmpty());
    }
}
