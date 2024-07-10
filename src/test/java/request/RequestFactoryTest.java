package request;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.auth.AuthRequest;
import request.auth.LogoutRequest;
import request.mail.MailWriteRequest;
import request.mail.MailsDeleteRequest;
import request.mail.MailsReadRequest;
import request.mail.ServerDetailsRequest;
import request.user.UserChangePasswordRequest;
import request.user.UserChangeRoleRequest;
import request.user.UserRemoveRequest;
import request.user.UserSwitchRequest;
import shared.UserInput;
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
        when(mockUserInput.getUsername()).thenReturn("testUser");
        when(mockUserInput.getPassword()).thenReturn("testPassword");

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
        when(mockUserInput.getRecipient()).thenReturn("recipient");
        when(mockUserInput.getMessage()).thenReturn("message");

        Request request = factory.getRequest("WRITE");

        MailWriteRequest mailWriteRequest = (MailWriteRequest) request;
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

        MailsReadRequest mailsReadRequest = (MailsReadRequest) request;
        assertEquals("READ", mailsReadRequest.getCommand());
        assertEquals("OPENED", mailsReadRequest.getBoxType());
    }
    @Test
    @DisplayName("Should test MailboxDeleteRequest return")
    public void testGetMailboxDeleteRequest() throws IOException {
        when(mockUserInput.chooseBoxOperation()).thenReturn("DELETE");
        when(mockUserInput.chooseBoxType()).thenReturn("UNREAD");

        Request request = factory.getMailboxRequest();

        MailsDeleteRequest mailsDeleteRequest = (MailsDeleteRequest) request;
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

        UserSwitchRequest userSwitchRequest = (UserSwitchRequest) request;
        assertEquals("SWITCH", userSwitchRequest.getCommand());
        assertEquals("exampleUser", userSwitchRequest.getUserToSwitch());
    }

    @Test
    @DisplayName("Should test UserChangePasswordRequest return")
    public void testGetUserChangePasswordRequest() throws IOException {
        when(mockUserInput.chooseUpdateOperation()).thenReturn("PASSWORD");
        when(mockUserInput.chooseUserToUpdate()).thenReturn("exampleUser");
        when(mockUserInput.getNewPassword()).thenReturn("newPassword");

        Request request = factory.getUpdateRequest();

        UserChangePasswordRequest userChangePasswordRequest = (UserChangePasswordRequest) request;
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

        UserRemoveRequest userRemoveRequest = (UserRemoveRequest) request;
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

        UserChangeRoleRequest userChangeRoleRequest = (UserChangeRoleRequest) request;
        assertEquals("ROLE", userChangeRoleRequest.getCommand());
        assertEquals("exampleUser", userChangeRoleRequest.getUserToUpdate());
        assertEquals(User.Role.ADMIN, userChangeRoleRequest.getNewRole());
    }

}