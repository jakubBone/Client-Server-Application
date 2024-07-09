package resposne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.user.UserSwitchResponse;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserSwitchResponseTest {
    UserManager mockUserManager;
    UserSwitchResponse userSwitchResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockUserManager = mock(UserManager.class);
        userSwitchResponse = new UserSwitchResponse(mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test user not found response return")
    void testExecuteUserNotFound() {
        when(mockRequest.getUserToSwitch()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(null);

        String response = userSwitchResponse.execute(mockRequest);

        assertEquals(ResponseStatus.SWITCH_FAILED.getResponse() + ": user not found", response);
    }

    @Test
    @DisplayName("Should test user not authorized response return")
    void testExecuteUserNotAuthorized() {
        User mockUser = mock(User.class);
        when(mockRequest.getUserToSwitch()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);
        when(mockUserManager.isUserAdmin()).thenReturn(false);

        String response = userSwitchResponse.execute(mockRequest);

        assertEquals(ResponseStatus.SWITCH_FAILED.getResponse() + ": user not authorized", response);
    }

    @Test
    @DisplayName("Should test switch succeeded to admin role user uresponse return")
    void testExecuteSwitchSucceededAdmin() {
        User mockUser = mock(User.class);
        when(mockRequest.getUserToSwitch()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        UserManager.ifSwitchedToAdminUser = true;

        String response = userSwitchResponse.execute(mockRequest);

        verify(mockUserManager).switchUser(mockUser);
        assertEquals(ResponseStatus.SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE.getResponse(), response);
    }

    @Test
    @DisplayName("Should test switch succeeded to non-admin role user response return")
    void testExecuteSwitchSucceededNonAdmin() {
        User mockUser = mock(User.class);
        when(mockRequest.getUserToSwitch()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        UserManager.ifSwitchedToAdminUser = false;
        UserManager.ifSwitchedToNonAdminUser = true;

        String response = userSwitchResponse.execute(mockRequest);

        verify(mockUserManager).switchUser(mockUser);
        assertEquals(ResponseStatus.SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE.getResponse(), response);
    }
}
