package unit_test.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.InboxHandler;
import com.jakub.bone.domain.Mail;
import com.jakub.bone.domain.User;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InboxHandlerTest {
    MailService mockMailService;
    InboxHandler inboxHandler;
    CommandDTO commandDTO;
    User sender;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        inboxHandler = new InboxHandler(mockMailService);
        sender = new User("sender", "pass", User.Role.USER);
    }

    @Test
    @DisplayName("Should test InboxHandler with null boxType defaults to INBOX and returns mailbox empty")
    void testDefaultInbox_Empty() {
        commandDTO = CommandDTO.builder().commandType("INBOX").build();

        when(mockMailService.getMails("INBOX")).thenReturn(Collections.emptyList());

        String expected = ResponseStatus.MAILBOX_EMPTY.getResponse();
        String response = inboxHandler.execute(commandDTO);
        verify(mockMailService, times(1)).getMails("INBOX");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test InboxHandler with non-empty mailbox returns formatted messages")
    void testNonEmptyInbox() {
        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(Map.of("boxType", "INBOX"))
                .build();

        Mail mail = new Mail(sender, new User("recipient", "pass", User.Role.USER), "Hello", LocalDateTime.now());
        List<Mail> mails = List.of(mail);
        when(mockMailService.getMails("INBOX")).thenReturn(mails);

        String response = inboxHandler.execute(commandDTO);
        // Response should contains "From:" because it is INBOX
        assertTrue(response.contains("From:"));
        assertTrue(response.contains("Hello"));
    }
}
