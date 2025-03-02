package unit_test.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.DeleteMailHandler;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteMailHandlerTest {
    MailService mockMailService;
    DeleteMailHandler deleteMailHandler;
    CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        deleteMailHandler = new DeleteMailHandler(mockMailService);
    }

    @Test
    @DisplayName("Should test DeleteMailHandler with valid payload")
    void testDeleteMailsValid() {
        Map<String, String> payload = new HashMap<>();
        payload.put("username", "newUser");
        payload.put("password", "pass123");

        commandDTO = CommandDTO.builder()
                .commandType("DELETE")
                .payload(Map.of("boxType", "INBOX"))
                .build();

        String expected = ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse();
        String response = deleteMailHandler.execute(commandDTO);
        verify(mockMailService, times(1)).deleteMails("INBOX");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test DeleteMailHandler with missing payload returns unknown request")
    void testDeleteMailsMissingPayload() {
        commandDTO = CommandDTO.builder()
                .commandType("DELETE")
                // no payload for boxType provided
                .build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = deleteMailHandler.execute(commandDTO);
        verify(mockMailService, never()).deleteMails(anyString());
        assertEquals(expected, response);
    }
}
