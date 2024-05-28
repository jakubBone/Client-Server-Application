
import mail.Mail;
import mail.MailBox;
import mail.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserManager;

import java.util.List;


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

        Assertions.assertTrue(sender.getMailBox().getSentMails().contains(mail));
        Assertions.assertTrue(recipient.getMailBox().getUnreadMails().contains(mail));
    }

    @Test
    @DisplayName("Should test specific mail list returning")
    void testGetMailsToRead() {
        String requestedMailBox = "UNREAD";

        List<Mail> mailList = mailService.getMailsToRead(requestedMailBox);

        Assertions.assertTrue(mailBox.getUnreadMails().equals(mailList));
    }

    @Test
    @DisplayName("Should test empty mailbox")
    void testEmptyMailbox() {
        String requestedMailBox = "OPEN";

        mailService.emptyMailbox(requestedMailBox);

        Assertions.assertTrue(mailBox.getOpenedMails().isEmpty());
    }

    @Test
    @DisplayName("Should test specific mail list returning")
    void testMarkMailsAsRead() {
        String requestedMailBox = "UNREAD";
        List<Mail> mailList = List.of(mail);
        mailBox.setUnreadMails(mailList);

        Assertions.assertFalse(mailBox.getUnreadMails().isEmpty());
        mailService.markMailsAsRead(requestedMailBox);
        Assertions.assertTrue(mailBox.getSentMails().isEmpty());
    }
}
