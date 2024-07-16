package resposne.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.user.UserRoleChangeResponse;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserRoleChangeResponseTest {
    UserManager mockUserManager;
    UserRoleChangeResponse userRoleChangeResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockUserManager = mock(UserManager.class);
        userRoleChangeResponse = new UserRoleChangeResponse(mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test authorization failed response return")
    void testExecuteAuthorizationFailed() {
        when(mockUserManager.isUserAdmin()).thenReturn(false);

        String response = userRoleChangeResponse.execute(mockRequest);

        assertEquals(ResponseStatus.AUTHORIZATION_FAILED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test user not found response return")
    void testExecuteUserNotFound() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(null);

        String response = userRoleChangeResponse.execute(mockRequest);

        assertEquals(ResponseStatus.FAILED_TO_FIND_USER.getResponse(), response);
    }

    @Test
    @DisplayName("Should test role change succeeded response return")
    void testExecuteRoleChangeSucceeded() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        User mockUser = mock(User.class);
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);
        when(mockRequest.getNewRole()).thenReturn(User.Role.ADMIN);

        String response = userRoleChangeResponse.execute(mockRequest);

        verify(mockUserManager).changeUserRole(mockUser, User.Role.ADMIN);
        assertEquals(ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse(), response);
    }
}
