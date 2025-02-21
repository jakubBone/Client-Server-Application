package request;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.auth.AuthRequest;
import request.auth.LogoutRequest;
import request.mail.InboxRequest;
import request.mail.NewMailRequest;
import request.mail.DeleteMailRequest;
import request.mail.ServerDetailsRequest;
import request.user.ChangePasswordRequest;
import request.user.AssignRoleRequest;
import request.user.RemoveUserRequest;
import request.user.SwitchUserRequest;
import ui.UserInput;
import client.ClientConnection;
import user.credential.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestFactoryTest {
    UserInput mockUserInput;
    ClientConnection mockClientConnection;
    RequestFactory factory;

    @BeforeEach
    void setUp() {
        mockUserInput = mock(UserInput.class);
        mockClientConnection = mock(ClientConnection.class);
        factory = new RequestFactory(mockClientConnection);
        factory.setUserInput(mockUserInput);
    }

    @Test
    @DisplayName("Should test authRequest return")
    public void testGetAuthRequest() throws IOException {
        when(mockUserInput.promptUsername()).thenReturn("testUser");
        when(mockUserInput.promptPassword()).thenReturn("testPassword");

        AuthRequest request = (AuthRequest) factory.getRequest("LOGIN");

        assertNotNull(request);
        assertEquals("LOGIN", request.getCommand());
        assertEquals("testUser", request.getUsername());
        assertEquals("testPassword", request.getPassword());
    }

    @Test
    @DisplayName("Should test logoutRequest return")
    public void testGetLogoutRequest() throws IOException {
        Request request = factory.getRequest("LOGOUT");
        LogoutRequest logoutRequest = (LogoutRequest) request;
        assertEquals("LOGOUT", logoutRequest.getCommand());
    }


    @Test
    @DisplayName("Should test MailWriteRequest return")
    public void testGetMailWriteRequest() throws IOException {
        when(mockUserInput.promptRecipient()).thenReturn("recipient");
        when(mockUserInput.promptMessage()).thenReturn("message");

        Request request = factory.getRequest("WRITE");

        NewMailRequest mailWriteRequest = (NewMailRequest) request;
        assertEquals("WRITE", mailWriteRequest.getCommand());
        assertEquals("recipient", mailWriteRequest.getRecipient());
        assertEquals("message", mailWriteRequest.getMessage());
    }

    @Test
    @DisplayName("Should test MailboxReadRequest return")
    public void testGetMailboxReadRequest() throws IOException {
        when(mockUserInput.chooseBoxOperation()).thenReturn("READ");
        when(mockUserInput.chooseBoxType()).thenReturn("OPENED");

        Request request = factory.getMailboxRequest();

        InboxRequest mailsReadRequest = (InboxRequest) request;
        assertEquals("READ", mailsReadRequest.getCommand());
        assertEquals("OPENED", mailsReadRequest.getBoxType());
    }
    @Test
    @DisplayName("Should test MailboxDeleteRequest return")
    public void testGetMailboxDeleteRequest() throws IOException {
        when(mockUserInput.chooseBoxOperation()).thenReturn("DELETE");
        when(mockUserInput.chooseBoxType()).thenReturn("UNREAD");

        Request request = factory.getMailboxRequest();

        DeleteMailRequest mailsDeleteRequest = (DeleteMailRequest) request;
        assertEquals("DELETE", mailsDeleteRequest.getCommand());
        assertEquals("UNREAD", mailsDeleteRequest.getBoxType());
    }

    @Test
    @DisplayName("Should test ServerDetailsRequest return")
    public void testGetServerDetailsRequest() throws IOException {
        Request request = factory.getRequest("INFO");

        ServerDetailsRequest serverDetailsRequest = (ServerDetailsRequest) request;
        assertEquals("INFO", serverDetailsRequest.getCommand());
    }

    @Test
    @DisplayName("Should test UserSwitchRequest return")
    public void testGetUserSwitchRequest() throws IOException {
        when(mockUserInput.getUserToSwitch()).thenReturn("exampleUser");

        Request request = factory.getRequest("SWITCH");

        SwitchUserRequest userSwitchRequest = (SwitchUserRequest) request;
        assertEquals("SWITCH", userSwitchRequest.getCommand());
        assertEquals("exampleUser", userSwitchRequest.getUserToSwitch());
    }

    @Test
    @DisplayName("Should test UserChangePasswordRequest return")
    public void testGetUserChangePasswordRequest() throws IOException {
        when(mockUserInput.chooseUpdateOperation()).thenReturn("PASSWORD");
        when(mockUserInput.chooseUserToUpdate()).thenReturn("exampleUser");
        when(mockUserInput.promptNewPassword()).thenReturn("newPassword");

        Request request = factory.getUpdateRequest();

        ChangePasswordRequest userChangePasswordRequest = (ChangePasswordRequest) request;
        assertEquals("PASSWORD", userChangePasswordRequest.getCommand());
        assertEquals("exampleUser", userChangePasswordRequest.getUserToUpdate());
        assertEquals("newPassword", userChangePasswordRequest.getNewPassword());
    }

    @Test
    @DisplayName("Should test UserRemoveRequest return")
    public void testGetUserRemoveRequest() throws IOException {
        when(mockUserInput.chooseUpdateOperation()).thenReturn("REMOVE");
        when(mockUserInput.chooseUserToUpdate()).thenReturn("exampleUser");

        Request request = factory.getUpdateRequest();

        RemoveUserRequest userRemoveRequest = (RemoveUserRequest) request;
        assertEquals("REMOVE", userRemoveRequest.getCommand());
        assertEquals("exampleUser", userRemoveRequest.getUserToUpdate());
    }

    @Test
    @DisplayName("Should test UserChangeRoleRequest return")
    public void testGetUserChangeRoleRequest() throws IOException {
        when(mockUserInput.chooseUpdateOperation()).thenReturn("ROLE");
        when(mockUserInput.chooseUserToUpdate()).thenReturn("exampleUser");
        when(mockUserInput.chooseRole()).thenReturn(User.Role.ADMIN);

        Request request = factory.getUpdateRequest();

        AssignRoleRequest userChangeRoleRequest = (AssignRoleRequest) request;
        assertEquals("ROLE", userChangeRoleRequest.getCommand());
        assertEquals("exampleUser", userChangeRoleRequest.getUserToUpdate());
        assertEquals(User.Role.ADMIN, userChangeRoleRequest.getNewRole());
    }

}