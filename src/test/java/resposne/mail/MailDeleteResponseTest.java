package resposne.mail;

import mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import servercommand.DeleteMailServerCommand;
import utils.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MailDeleteResponseTest {
    MailService mockMailService;
    DeleteMailServerCommand mailsDeleteResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        mailsDeleteResponse = new DeleteMailServerCommand(mockMailService);
        mockRequest = mock(Request.class);
    }
    @Test
    @DisplayName("Should test mails deletion and return success response")
    void testExecute() {
        when(mockRequest.getBoxType()).thenReturn("UNREAD");

        String response = mailsDeleteResponse.execute(mockRequest);

        verify(mockMailService).deleteMails("UNREAD");
        assertEquals(ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse(), response);
    }
}
