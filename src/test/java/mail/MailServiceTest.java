package mail;

import com.jakub.bone.domain.model.Mail;
import com.jakub.bone.repository.MailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jakub.bone.domain.model.User;
import com.jakub.bone.application.service.MailService;
import com.jakub.bone.application.service.UserService;

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
    MailRepository mockMailDAO;

    @BeforeEach
    void setUp() {
        mockMailDAO = mock(MailRepository.class);
        mailService = new MailService();
        mailService.setMailDAO(mockMailDAO);
        sender = new User("senderName", "testPassword", User.Role.USER);
        UserService.currentLoggedInUser = sender;
        recipient = new User("recipientName", "testPassword", User.Role.USER);
        mail = new Mail(sender, recipient, "test message", Mail.Status.UNREAD);
    }

    @Test
    @DisplayName("Should test mail sending")
    void testSendMail() {
        mailService.sendMail(recipient, "message");

        // verify if mail has been saved in DB
        verify(mockMailDAO, times(2)).saveMail(any(Mail.class));
    }

    @Test
    @DisplayName("Should test getting mails from the database")
    void getMails() {
        String mailbox = "UNREAD";
        List<Mail> expectedMails = List.of(mail);

        when(mockMailDAO.findMails(mailbox)).thenReturn(expectedMails);

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

        verify(mockMailDAO,times(1)).deleteMails(mailbox);
    }

}
