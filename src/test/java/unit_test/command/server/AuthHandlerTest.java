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
    AuthService mockAuthService;
    UserService mockUserService;
    AuthHandler authHandler;
    CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mockAuthService = mock(AuthService.class);
        mockUserService = mock(UserService.class);
        authHandler = new AuthHandler(mockAuthService, mockUserService);
    }

    @Test
    @DisplayName("Should test AuthHandler with REGISTER command")
    void testRegisterCommand() {
        // Prepare payload
        commandDTO = new CommandDTO.Builder()
                .commandType("REGISTER")
                .addPayload("username", "newUser")
                .addPayload("password", "pass123")
                .build();

        // Stub the authService.register call
        when(mockAuthService.register("newUser", "pass123"))
                .thenReturn(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse());

        String response = authHandler.execute(commandDTO);
        verify(mockAuthService, times(1)).register("newUser", "pass123");
        assertEquals(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse(), response);
    }

    @Test
    @DisplayName("Should test AuthHandler with LOGIN command")
    void testLoginCommand() {
        commandDTO = new CommandDTO.Builder()
                .commandType("LOGIN")
                .addPayload("username", "user1")
                .addPayload("password", "pass123")
                .build();

        when(mockAuthService.login("user1", "pass123"))
                .thenReturn(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse());

        String response = authHandler.execute(commandDTO);
        verify(mockAuthService, times(1)).login("user1", "pass123");
        assertEquals(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse(), response);
    }
}
