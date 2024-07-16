package resposne.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.auth.RegisterResponse;
import user.manager.AuthManager;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterResponseTest {
    AuthManager mockAuthManager;
    UserManager mockUserManager;
    RegisterResponse registerResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockAuthManager = mock(AuthManager.class);
        mockUserManager = mock(UserManager.class);
        registerResponse = new RegisterResponse(mockAuthManager, mockUserManager);
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
