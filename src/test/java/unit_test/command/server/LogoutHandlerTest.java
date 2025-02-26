package unit_test.command.server;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.LogoutHandler;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LogoutHandlerTest {
    private AuthService authService;
    private LogoutHandler logoutHandler;
    private CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        logoutHandler = new LogoutHandler(authService);
        // No payload required for logout command
        commandDTO = new CommandDTO.Builder().commandType("LOGOUT").build();
    }

    @Test
    @DisplayName("LogoutHandler executes logout")
    void testLogout() {
        when(authService.logout()).thenReturn(ResponseStatus.LOGOUT_SUCCEEDED.getResponse());

        String response = logoutHandler.execute(commandDTO);
        verify(authService, times(1)).logout();
        assertEquals(ResponseStatus.LOGOUT_SUCCEEDED.getResponse(), response);
    }
}
