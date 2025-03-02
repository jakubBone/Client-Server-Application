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
    AuthService mockAuthService;
    LogoutHandler logoutHandler;
    CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mockAuthService = mock(AuthService.class);
        logoutHandler = new LogoutHandler(mockAuthService);
        commandDTO = CommandDTO.builder().commandType("LOGOUT").build();
    }

    @Test
    @DisplayName("Should test LogoutHandler executes logout")
    void testLogout() {
        when(mockAuthService.logout()).thenReturn(ResponseStatus.LOGOUT_SUCCEEDED.getResponse());

        String response = logoutHandler.execute(commandDTO);
        verify(mockAuthService, times(1)).logout();
        assertEquals(ResponseStatus.LOGOUT_SUCCEEDED.getResponse(), response);
    }
}
