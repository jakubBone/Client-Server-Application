package command.server;

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
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InboxHandlerTest {
    private MailService mailService;
    private InboxHandler inboxHandler;
    private CommandDTO commandDTO;
    private User sender;

    @BeforeEach
    void setUp() {
        mailService = mock(MailService.class);
        inboxHandler = new InboxHandler(mailService);
        sender = new User("sender", "pass", User.Role.USER);
    }

    @Test
    @DisplayName("InboxHandler - null boxType defaults to INBOX and returns mailbox empty")
    void testDefaultInbox_Empty() {
        commandDTO = new CommandDTO.Builder()
                .commandType("INBOX")
                .build();
        when(mailService.getMails("INBOX")).thenReturn(Collections.emptyList());

        String expected = ResponseStatus.MAILBOX_EMPTY.getResponse();
        String response = inboxHandler.execute(commandDTO);
        verify(mailService, times(1)).getMails("INBOX");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("InboxHandler - non-empty mailbox returns formatted messages")
    void testNonEmptyInbox() {
        commandDTO = new CommandDTO.Builder()
                .commandType("INBOX")
                .addPayload("boxType", "INBOX")
                .build();

        Mail mail = new Mail(sender, new User("recipient", "pass", User.Role.USER), "Hello", LocalDateTime.now());
        List<Mail> mails = List.of(mail);
        when(mailService.getMails("INBOX")).thenReturn(mails);

        String response = inboxHandler.execute(commandDTO);
        // Check that the response contains "From:" because it is INBOX
        assertTrue(response.contains("From:"));
        assertTrue(response.contains("Hello"));
    }
}
