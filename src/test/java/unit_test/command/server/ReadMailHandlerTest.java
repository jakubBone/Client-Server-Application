package unit_test.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.ReadMailHandler;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.domain.User;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadMailHandlerTest {
    MailService mockMailService;
    ReadMailHandler readMailHandler;
    CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        readMailHandler = new ReadMailHandler(mockMailService);
    }

    @Test
    @DisplayName("Should test ReadMailHandler with missing boxType returns unknown request")
    void testMissingBoxType() {
        commandDTO = CommandDTO.builder()
                .commandType("READ")
                // boxType not provided
                .build();

        String expected = com.jakub.bone.utils.ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = readMailHandler.execute(commandDTO);
        verify(mockMailService, never()).getMails(anyString());
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test ReadMailHandler with empty mailbox returns mailbox empty")
    void testEmptyMailbox() {
        commandDTO = CommandDTO.builder()
                .commandType("READ")
                .payload(Map.of("boxType", "INBOX"))
                .build();

        when(mockMailService.getMails("INBOX")).thenReturn(Collections.emptyList());

        String expected = ResponseStatus.MAILBOX_EMPTY.getResponse();
        String response = readMailHandler.execute(commandDTO);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test ReadMailHandler with non-empty mailbox returns formatted messages")
    void testNonEmptyMailbox() {
        commandDTO = CommandDTO.builder()
                .commandType("READ")
                .payload(Map.of("boxType", "SENT"))
                .build();

        // Create mocks for sender and recipient
        User mockSender = mock(User.class);
        User mockRecipient = mock(User.class);
        when(mockRecipient.getUsername()).thenReturn("recipient");

        // Create a mock for Mail and stub its methods
        Mail mockMail = mock(Mail.class);
        when(mockMail.getRecipient()).thenReturn(mockRecipient);
        when(mockMail.getSender()).thenReturn(mockSender);
        when(mockMail.getMessage()).thenReturn("Test message");

        List<Mail> mails = List.of(mockMail);
        when(mockMailService.getMails("SENT")).thenReturn(mails);

        String response = readMailHandler.execute(commandDTO);

        // Response should contains "To:" because it's a SENT
        assertTrue(response.contains("To:"));
        assertTrue(response.contains("Test message"));
    }
}
