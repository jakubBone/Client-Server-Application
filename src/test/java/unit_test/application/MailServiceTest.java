package unit_test.application;

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
    MailService mailService;
    MailRepository mockMailRepository;
    UserRepository mockUserRepository;
    SessionManager sessionManager;
    DSLContext context;
    User currentUser;
    User recipient;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        // Set the current user in session
        currentUser = new User("sender", "pass", User.Role.USER);
        sessionManager.setCurrentUser(currentUser);

        mockMailRepository = mock(MailRepository.class);
        mockUserRepository = mock(UserRepository.class);

        // Using a null DSLContext as we are not testing JOOQ behavior
        context = null;

        mailService = new MailService(sessionManager, mockMailRepository);
        //mailService.setMailRepository(mailRepository);
    }

    @Test
    @DisplayName("Should test sending mail success")
    void testSendMailSuccess() {
        recipient = new User("recipient", "pass", User.Role.USER);
        String message = "Test message";

        when(mockMailRepository.isMailboxFull(recipient)).thenReturn(false);

        String response = mailService.sendMail(recipient, message);

        verify(mockMailRepository, times(1)).saveMail(any(Mail.class));
        assertEquals(ResponseStatus.SENDING_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test sending mail when mailbox is full")
    void testSendMailMailboxFullFailure() {
        recipient = new User("recipient", "pass", User.Role.USER);
        String message = "Test message";

        when(mockMailRepository.isMailboxFull(recipient)).thenReturn(true);

        String response = mailService.sendMail(recipient, message);

        verify(mockMailRepository, never()).saveMail(any(Mail.class));
        assertEquals(ResponseStatus.SENDING_FAILED_BOX_FULL.getResponse(), response);
    }

    @Test
    @DisplayName("Should test retrieving mails with non-empty list")
    void testGetMailsNonEmptyList() {
        String boxType = "INBOX";
        Mail mail = new Mail(currentUser, recipient = new User("recipient", "pass", User.Role.USER), "Test", LocalDateTime.now());
        List<Mail> mails = Arrays.asList(mail);
        when(mockMailRepository.findMails(boxType, sessionManager)).thenReturn(mails);

        List<Mail> result = mailService.getMails(boxType);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should test retrieving mails with empty list")
    void testGetMailsEmptyList() {
        String boxType = "INBOX";
        when(mockMailRepository.findMails(boxType, sessionManager)).thenReturn(Collections.emptyList());

        List<Mail> result = mailService.getMails(boxType);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should test deleting mails")
    void testDeleteMails() {
        String boxType = "SENT";

        mailService.deleteMails(boxType);

        verify(mockMailRepository, times(1)).deleteMails(boxType, sessionManager);
    }
}
