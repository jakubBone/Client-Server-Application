package resposne.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.auth.RegisterServerCommand;
import com.jakub.bone.application.service.AuthService;
import com.jakub.bone.application.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterResponseTest {
    AuthService mockAuthManager;
    UserService mockUserManager;
    RegisterServerCommand registerResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockAuthManager = mock(AuthService.class);
        mockUserManager = mock(UserService.class);
        registerResponse = new RegisterServerCommand(mockAuthManager, mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test correct register response return")
    void testExecute() {
        when(mockRequest.getUsername()).thenReturn("testUser");
        when(mockRequest.getPassword()).thenReturn("testPassword");
        when(mockAuthManager.registerAndGetResponse("testUser", "testPassword", mockUserManager))
                .thenReturn("Registration successful");

        String response = registerResponse.execute(mockRequest);

        assertEquals("Registration successful", response);
    }
}
