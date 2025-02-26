package application;

import com.jakub.bone.application.MailService;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.domain.User;
import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MailServiceTest {

    private MailService mailService;
    private MailRepository mailRepository;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private DSLContext dslContext;

    private User currentUser;
    private User recipient;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        // Set the current user in session
        currentUser = new User("sender", "pass", User.Role.USER);
        sessionManager.setCurrentUser(currentUser);

        mailRepository = mock(MailRepository.class);
        userRepository = mock(UserRepository.class);

        // Using a null DSLContext as we are not testing JOOQ behavior
        dslContext = null;

        mailService = new MailService(sessionManager, mailRepository);
        //mailService.setMailRepository(mailRepository);
    }

    @Test
    @DisplayName("Sending mail - success")
    void sendMail_Success() {
        recipient = new User("recipient", "pass", User.Role.USER);
        String message = "Test message";

        // Recipient's mailbox is not full
        when(mailRepository.isMailboxFull(recipient)).thenReturn(false);

        String response = mailService.sendMail(recipient, message);

        // Verify that saveMail was executed
        verify(mailRepository, times(1)).saveMail(any(Mail.class));
        assertEquals(ResponseStatus.SENDING_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Sending mail when mailbox is full - failure")
    void sendMail_MailboxFull_Failure() {
        recipient = new User("recipient", "pass", User.Role.USER);
        String message = "Test message";

        when(mailRepository.isMailboxFull(recipient)).thenReturn(true);

        String response = mailService.sendMail(recipient, message);

        // saveMail should not be called
        verify(mailRepository, never()).saveMail(any(Mail.class));
        assertEquals(ResponseStatus.SENDING_FAILED_BOX_FULL.getResponse(), response);
    }

    @Test
    @DisplayName("Retrieving mails - non-empty list")
    void getMails_NonEmptyList() {
        String boxType = "INBOX";
        Mail mail1 = new Mail(currentUser, recipient = new User("recipient", "pass", User.Role.USER), "Test", LocalDateTime.now());
        List<Mail> mails = Arrays.asList(mail1);
        when(mailRepository.findMails(boxType, sessionManager)).thenReturn(mails);

        List<Mail> result = mailService.getMails(boxType);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Retrieving mails - empty list")
    void getMails_EmptyList() {
        String boxType = "INBOX";
        when(mailRepository.findMails(boxType, sessionManager)).thenReturn(Collections.emptyList());

        List<Mail> result = mailService.getMails(boxType);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deleting mails")
    void deleteMails_Test() {
        String boxType = "SENT";
        // Call deleteMails method
        mailService.deleteMails(boxType);

        // Verify that the repository's deleteMails method was called
        verify(mailRepository, times(1)).deleteMails(boxType, sessionManager);
    }
}
