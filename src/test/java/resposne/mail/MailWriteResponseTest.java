package resposne.mail;

import mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.mail.MailWriteResponse;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MailWriteResponseTest {
    MailService mockMailService;
    UserManager mockUserManager;
    MailWriteResponse mailWriteResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        mockUserManager = mock(UserManager.class);
        mailWriteResponse = new MailWriteResponse(mockMailService, mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test recipient not found response return")
    void testExecuteRecipientNotFound() {
        when(mockRequest.getRecipient()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(null);

        String response = mailWriteResponse.execute(mockRequest);

        assertEquals(ResponseStatus.SENDING_FAILED_RECIPIENT_NOT_FOUND.getResponse(), response);
    }

    @Test
    @DisplayName("Should test mailbox full response return")
    void testExecuteMailboxFull() {
        User recipient = new User("testUser", "password", User.Role.USER);
        when(mockRequest.getRecipient()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(recipient);
        when(mockMailService.isMailboxFull(recipient)).thenReturn(true);

        String response = mailWriteResponse.execute(mockRequest);

        assertEquals(ResponseStatus.SENDING_FAILED_BOX_FULL.getResponse(), response);
    }

    @Test
    @DisplayName("Should test message too long response return")
    void testExecuteMessageTooLong() {
        User recipient = new User("testUser", "password", User.Role.USER);
        when(mockRequest.getRecipient()).thenReturn("testUser");
        when(mockRequest.getMessage()).thenReturn("a".repeat(256));
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(recipient);

        String response = mailWriteResponse.execute(mockRequest);

        assertEquals(ResponseStatus.SENDING_FAILED_TO_LONG_MESSAGE.getResponse(), response);
    }

    @Test
    @DisplayName("Should test sending succeeded response return")
    void testExecuteSendingSucceeded() {
        User recipient = new User("testUser", "password", User.Role.USER);
        when(mockRequest.getRecipient()).thenReturn("testUser");
        when(mockRequest.getMessage()).thenReturn("Test message");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(recipient);
        when(mockMailService.isMailboxFull(recipient)).thenReturn(false);

        String response = mailWriteResponse.execute(mockRequest);

        verify(mockMailService).sendMail(recipient, "Test message");
        assertEquals(ResponseStatus.SENDING_SUCCEEDED.getResponse(), response);
    }
}
