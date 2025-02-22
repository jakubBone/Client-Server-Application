package resposne.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.auth.LoginServerCommand;
import com.jakub.bone.application.service.AuthService;
import com.jakub.bone.application.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginResponseTest {
    AuthService mockAuthManager;
    UserService mockUserManager;
    LoginServerCommand loginResponse;
    private Request mockRequest;

    @BeforeEach
    void setUp() {
        mockAuthManager = mock(AuthService.class);
        mockUserManager = mock(UserService.class);
        loginResponse = new LoginServerCommand(mockAuthManager, mockUserManager);
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
