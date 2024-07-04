import database.MailDAO;
import mail.Mail;
import mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.credential.User;
import user.manager.UserManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {
    User sender;
    User recipient;
    Mail mail;
    MailService mailService;
    MailDAO mockMailDAO;

    @BeforeEach
    void setUp() {
        mockMailDAO = mock(MailDAO.class);
        mailService = new MailService();
        sender = new User("senderName", "examplePassword", User.Role.USER);
        recipient = new User("recipientName", "examplePassword", User.Role.USER);
        mail = new Mail(sender, recipient, "Example message", Mail.Status.UNREAD);
    }

    @Test
    @DisplayName("Should test mail sending")
    void testSendMail() {
        mailService.sendMail(recipient, "message");

        // verify if mail has been saved in DB
        verify(mockMailDAO, times(2)).saveMailToDB(any(Mail.class));
    }

    @Test
    @DisplayName("Should test getting mails from the database")
    void getMails() {
        String mailbox = "UNREAD";
        List<Mail> expectedMails = List.of(mail);

        when(mockMailDAO.getMailsFromDB(mailbox)).thenReturn(expectedMails);

        List<Mail> mails = mailService.getMails(mailbox);

        assertEquals(expectedMails, mails);
    }

    @Test
    @DisplayName("Should test if mailbox is full in the database")
    void testisMailboxFull() {
        when(mailService.isMailboxFull(recipient)).thenReturn(true);
        boolean isFull = mailService.isMailboxFull(recipient);

        assertTrue(isFull);
    }

    @Test
    @DisplayName("Should test deleting mails from the database")
    void testDeleteMails() {
        String mailbox = "UNREAD";

        mailService.deleteMails(mailbox);

        verify(mockMailDAO,times(1)).deleteMailsFromDB(mailbox);
    }

    @Test
    @DisplayName("Should test if message is marked as read in the database")
    void testMarkAsRead() {
        mailService.markAsRead();

        verify(mockMailDAO,times(1)).markAsReadInDB();
    }
}