package unit_test.command.server;

import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.NewMailHandler;
import com.jakub.bone.domain.User;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NewMailHandlerTest {
    MailService mockMailService;
    UserService mockUserService;
    NewMailHandler newMailHandler;
    CommandDTO commandDTO;
    User recipient;

    @BeforeEach
    void setUp() {
        mockMailService = mock(MailService.class);
        mockUserService = mock(UserService.class);
        newMailHandler = new NewMailHandler(mockMailService, mockUserService);
        recipient = new User("recipient", "pass", User.Role.USER);
        when(mockUserService.findUserByUsername("recipient")).thenReturn(recipient);
    }

    @Test
    @DisplayName("Should test NewMailHandler executes sending mail")
    void testNewMailHandler() {
        commandDTO = new CommandDTO.Builder()
                .commandType("NEW")
                .addPayload("recipient", "recipient")
                .addPayload("message", "Hello Mail")
                .build();

        when(mockMailService.sendMail(recipient, "Hello Mail"))
                .thenReturn(ResponseStatus.SENDING_SUCCEEDED.getResponse());

        String response = newMailHandler.execute(commandDTO);
        verify(mockMailService, times(1)).sendMail(recipient, "Hello Mail");
        assertEquals(ResponseStatus.SENDING_SUCCEEDED.getResponse(), response);
    }
}
