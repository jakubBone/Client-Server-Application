package resposne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.auth.LoginResponse;
import user.manager.AuthManager;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginResponseTest {
    AuthManager mockAuthManager;
    UserManager mockUserManager;
    LoginResponse loginResponse;
    private Request mockRequest;

    @BeforeEach
    void setUp() {
        mockAuthManager = mock(AuthManager.class);
        mockUserManager = mock(UserManager.class);
        loginResponse = new LoginResponse(mockAuthManager, mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test correct login response return")
    void testExecute() {
        when(mockRequest.getUsername()).thenReturn("testUser");
        when(mockRequest.getPassword()).thenReturn("testPassword");
        when(mockAuthManager.loginAndGetResponse("testUser", "testPassword", mockUserManager))
                .thenReturn("User login successful");

        String response = loginResponse.execute(mockRequest);

        assertEquals("User login successful", response);
    }
}
