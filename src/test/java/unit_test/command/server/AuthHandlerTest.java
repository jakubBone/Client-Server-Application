package unit_test.command.server;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.AuthHandler;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthHandlerTest {
    private AuthService authService;
    private UserService userService;
    private AuthHandler authHandler;
    private CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        userService = mock(UserService.class);
        authHandler = new AuthHandler(authService, userService);
    }

    @Test
    @DisplayName("AuthHandler - REGISTER command")
    void testRegisterCommand() {
        // Prepare payload
        commandDTO = new CommandDTO.Builder()
                .commandType("REGISTER")
                .addPayload("username", "newUser")
                .addPayload("password", "pass123")
                .build();
        // Stub the authService.register call
        when(authService.register("newUser", "pass123"))
                .thenReturn(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse());

        String response = authHandler.execute(commandDTO);
        verify(authService, times(1)).register("newUser", "pass123"
        );
        assertEquals(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse(), response);
    }

    @Test
    @DisplayName("AuthHandler - LOGIN command")
    void testLoginCommand() {
        // Prepare payload
        commandDTO = new CommandDTO.Builder()
                .commandType("LOGIN")
                .addPayload("username", "user1")
                .addPayload("password", "pass123")
                .build();
        // Stub the authService.login call
        when(authService.login("user1", "pass123"))
                .thenReturn(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse());

        String response = authHandler.execute(commandDTO);
        verify(authService, times(1)).login("user1", "pass123");
        assertEquals(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse(), response);
    }
}
