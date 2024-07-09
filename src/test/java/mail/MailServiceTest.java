package mail;

import database.MailDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.credential.User;
import user.manager.UserManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class MailServiceTest {
    User sender;
    User recipient;
    Mail mail;
    MailService mailService;
    MailDAO mockMailDAO;

    @BeforeEach
    void setUp() {
        mockMailDAO = mock(MailDAO.class);
        mailService = new MailService();
        mailService.setMailDAO(mockMailDAO);
        sender = new User("senderName", "testPassword", User.Role.USER);
        UserManager.currentLoggedInUser = sender;
        recipient = new User("recipientName", "testPassword", User.Role.USER);
        mail = new Mail(sender, recipient, "test message", Mail.Status.UNREAD);
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
