package resposne.mail;

import com.jakub.bone.application.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import com.jakub.bone.command.server.DeleteMailHandler;
import com.jakub.bone.utils.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MailDeleteResponseTest {
    MailService mockMailService;
    DeleteMailHandler mailsDeleteResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        mailsDeleteResponse = new DeleteMailHandler(mockMailService);
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
