package command.server;

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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadMailHandlerTest {
    private MailService mailService;
    private ReadMailHandler readMailHandler;
    private CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mailService = mock(MailService.class);
        readMailHandler = new ReadMailHandler(mailService);
    }

    @Test
    @DisplayName("ReadMailHandler - missing boxType returns unknown request")
    void testMissingBoxType() {
        commandDTO = new CommandDTO.Builder()
                .commandType("READ")
                // boxType not provided
                .build();

        String expected = com.jakub.bone.utils.ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = readMailHandler.execute(commandDTO);
        verify(mailService, never()).getMails(anyString());
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("ReadMailHandler - empty mailbox returns mailbox empty")
    void testEmptyMailbox() {
        commandDTO = new CommandDTO.Builder()
                .commandType("READ")
                .addPayload("boxType", "INBOX")
                .build();

        when(mailService.getMails("INBOX")).thenReturn(Collections.emptyList());

        String expected = ResponseStatus.MAILBOX_EMPTY.getResponse();
        String response = readMailHandler.execute(commandDTO);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("ReadMailHandler - non-empty mailbox returns formatted messages")
    void testNonEmptyMailbox() {
        commandDTO = new CommandDTO.Builder()
                .commandType("READ")
                .addPayload("boxType", "SENT")
                .build();

        // Create mocks for sender and recipient
        User sender = mock(User.class);
        User recipient = mock(User.class);
        when(recipient.getUsername()).thenReturn("recipient");

        // Create a mock for Mail and stub its methods
        Mail mail = mock(Mail.class);
        when(mail.getRecipient()).thenReturn(recipient);
        when(mail.getSender()).thenReturn(sender);
        when(mail.getMessage()).thenReturn("Test message");

        List<Mail> mails = List.of(mail);
        when(mailService.getMails("SENT")).thenReturn(mails);

        String response = readMailHandler.execute(commandDTO);

        // Verify that the response contains "To:" because it's a SENT mailbox and includes the message.
        assertTrue(response.contains("To:"));
        assertTrue(response.contains("Test message"));
    }
}
