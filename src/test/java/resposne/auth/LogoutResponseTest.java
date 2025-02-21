package resposne.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.auth.LogoutServerCommand;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class LogoutResponseTest {
    UserManager mockUserManager;
    LogoutServerCommand logoutResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockUserManager = mock(UserManager.class);
        logoutResponse = new LogoutServerCommand(mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test correct logout response return")
    void testExecute() {
        when(mockUserManager.logoutAndGetResponse()).thenReturn("Logout succeeded");

        String response = logoutResponse.execute(mockRequest);

        assertEquals("Logout succeeded", response);
    }
}
