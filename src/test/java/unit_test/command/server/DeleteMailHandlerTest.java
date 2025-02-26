package unit_test.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.DeleteMailHandler;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteMailHandlerTest {
    MailService mailService;
    DeleteMailHandler deleteMailHandler;
    CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mailService = mock(MailService.class);
        deleteMailHandler = new DeleteMailHandler(mailService);
    }

    @Test
    @DisplayName("DeleteMailHandler - valid payload")
    void testDeleteMailsValid() {
        commandDTO = new CommandDTO.Builder()
                .commandType("DELETE")
                .addPayload("boxType", "INBOX")
                .build();

        // Assume that deleteMails returns void and handler returns a success message
        // We stub the response by returning a fixed success string after deletion.
        // (In actual implementation, the handler calls deleteMails and returns MAIL_DELETION_SUCCEEDED.)
        String expected = ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse();
        String response = deleteMailHandler.execute(commandDTO);
        verify(mailService, times(1)).deleteMails("INBOX");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("DeleteMailHandler - missing payload returns unknown request")
    void testDeleteMailsMissingPayload() {
        commandDTO = new CommandDTO.Builder()
                .commandType("DELETE")
                // no payload for boxType provided
                .build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = deleteMailHandler.execute(commandDTO);
        verify(mailService, never()).deleteMails(anyString());
        assertEquals(expected, response);
    }
}
