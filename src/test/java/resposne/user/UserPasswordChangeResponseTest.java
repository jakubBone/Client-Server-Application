package resposne.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.user.EditServerCommand;
import com.jakub.bone.utils.ResponseStatus;
import com.jakub.bone.domain.model.User;
import com.jakub.bone.application.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserPasswordChangeResponseTest {
    UserService mockUserManager;
    EditServerCommand userPasswordChangeResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockUserManager = mock(UserService.class);
        userPasswordChangeResponse = new EditServerCommand(mockUserManager);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test authorization failed response return")
    void testExecuteAuthorizationFailed() {
        when(mockUserManager.isUserAdmin()).thenReturn(false);

        String response = userPasswordChangeResponse.execute(mockRequest);

        assertEquals(ResponseStatus.AUTHORIZATION_FAILED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test user not found response return")
    void testExecuteUserNotFound() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(null);

        String response = userPasswordChangeResponse.execute(mockRequest);

        assertEquals(ResponseStatus.FAILED_TO_FIND_USER.getResponse(), response);
    }

    @Test
    @DisplayName("Should test operation succeeded response return")
    void testExecuteOperationSucceeded() {
        when(mockUserManager.isUserAdmin()).thenReturn(true);
        when(mockRequest.getUserToUpdate()).thenReturn("testUser");
        User mockUser = mock(User.class);
        when(mockUserManager.getUserByUsername("testUser")).thenReturn(mockUser);
        when(mockRequest.getNewPassword()).thenReturn("newPassword");

        String response = userPasswordChangeResponse.execute(mockRequest);

        verify(mockUserManager).changePassword(mockUser, "newPassword");
        assertEquals(ResponseStatus.OPERATION_SUCCEEDED.getResponse(), response);
    }
}
