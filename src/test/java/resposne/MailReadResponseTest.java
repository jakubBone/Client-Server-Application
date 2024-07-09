package response.mail;

import mail.Mail;
import mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import shared.ResponseStatus;
import user.credential.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MailReadResponseTest {
    MailService mockMailService;
    MailsReadResponse mailsReadResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        mailsReadResponse = new MailsReadResponse(mockMailService);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test mailbox empty response return when no mails")
    void testExecuteMailboxEmpty() {
        when(mockRequest.getBoxType()).thenReturn("UNREAD");
        when(mockMailService.getMails("UNREAD")).thenReturn(new ArrayList<>());

        String response = mailsReadResponse.execute(mockRequest);

        assertEquals(ResponseStatus.MAILBOX_EMPTY.getResponse(), response);
    }

    @Test
    @DisplayName("Should test mails return from inbox")
    void testExecuteWithMails() {
        when(mockRequest.getBoxType()).thenReturn("UNREAD");
        User sender = new User("senderName", "examplePassword", User.Role.USER);
        User recipient = new User("recipientName", "examplePassword", User.Role.USER);
        List<Mail> mails = List.of(new Mail(sender, recipient, "Example message", Mail.Status.UNREAD));
        when(mockMailService.getMails("UNREAD")).thenReturn(mails);

        String response = mailsReadResponse.execute(mockRequest);

        String expectedResponse = "UNREAD MAILS: \nFrom: senderName\n Message: Example message\n";
        assertEquals(expectedResponse, response);
    }
}
