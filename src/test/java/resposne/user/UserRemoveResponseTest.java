package resposne.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.user.UserRemoveResponse;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRemoveResponseTest {
    UserManager mockUserManager;
    UserRemoveResponse userRemoveResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockUserManager = mock(UserManager.class);
        userRemoveResponse = new UserRemoveResponse(mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test authorization failed response return")
    void testExecuteAuthorizationFailed() {
        when(mockUserManager.isUserAdmin()).thenReturn(false);

        String response = userRemoveResponse.execute(mockRequest);

        assertEquals(ResponseStatus.AUTHORIZATION_FAILED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test user not found response return")
    void testExecuteUserNotFound() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(null);

        String response = userRemoveResponse.execute(mockRequest);

        assertEquals(ResponseStatus.FAILED_TO_FIND_USER.getResponse(), response);
    }

    @Test
    @DisplayName("Should test operation succeeded response return")
    void testExecuteOperationSucceeded() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        User mockUser = mock(User.class);
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);

        String response = userRemoveResponse.execute(mockRequest);

        verify(mockUserManager).removeUser(mockUser);
        assertEquals(ResponseStatus.OPERATION_SUCCEEDED.getResponse(), response);
    }
}
