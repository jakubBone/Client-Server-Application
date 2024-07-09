package request;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import shared.UserInteraction;
import client.ClientConnection;
import user.credential.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestFacotryTest {
    UserInteraction mockUserInteraction;
    ClientConnection mockClientConnection;
    RequestFactory factory;

    @BeforeEach
    void setUp() {
        mockUserInteraction = mock(UserInteraction.class);
        mockClientConnection = mock(ClientConnection.class);
        factory = new RequestFactory(mockClientConnection);
        factory.setUserInteraction(mockUserInteraction);
    }

    @Test
    @DisplayName("Should test authRequest return")
    public void testGetAuthRequest() throws IOException {
        when(mockUserInteraction.getUsername()).thenReturn("testUser");
        when(mockUserInteraction.getPassword()).thenReturn("testPassword");

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
        when(mockUserInteraction.getRecipient()).thenReturn("recipient");
        when(mockUserInteraction.getMessage()).thenReturn("message");

        Request request = factory.getRequest("WRITE");

        MailWriteRequest mailWriteRequest = (MailWriteRequest) request;
        assertEquals("WRITE", mailWriteRequest.getCommand());
        assertEquals("recipient", mailWriteRequest.getRecipient());
        assertEquals("message", mailWriteRequest.getMessage());
    }

    @Test
    @DisplayName("Should test MailboxReadRequest return")
    public void testGetMailboxReadRequest() throws IOException {
        when(mockUserInteraction.chooseBoxOperation()).thenReturn("READ");
        when(mockUserInteraction.chooseBoxType()).thenReturn("OPENED");

        Request request = factory.getMailboxRequest();

        MailsReadRequest mailsReadRequest = (MailsReadRequest) request;
        assertEquals("READ", mailsReadRequest.getCommand());
        assertEquals("OPENED", mailsReadRequest.getBoxType());
    }
    @Test
    @DisplayName("Should test MailboxDeleteRequest return")
    public void testGetMailboxDeleteRequest() throws IOException {
        when(mockUserInteraction.chooseBoxOperation()).thenReturn("DELETE");
        when(mockUserInteraction.chooseBoxType()).thenReturn("UNREAD");

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
        when(mockUserInteraction.getUserToSwitch()).thenReturn("exampleUser");

        Request request = factory.getRequest("SWITCH");

        UserSwitchRequest userSwitchRequest = (UserSwitchRequest) request;
        assertEquals("SWITCH", userSwitchRequest.getCommand());
        assertEquals("exampleUser", userSwitchRequest.getUserToSwitch());
    }

    @Test
    @DisplayName("Should test UserChangePasswordRequest return")
    public void testGetUserChangePasswordRequest() throws IOException {
        when(mockClientConnection.isUserAuthorized()).thenReturn(true);
        when(mockUserInteraction.chooseUpdateOperation()).thenReturn("PASSWORD");
        when(mockUserInteraction.chooseUserToUpdate()).thenReturn("exampleUser");
        when(mockUserInteraction.getNewPassword()).thenReturn("newPassword");

        Request request = factory.getUpdateRequest();

        UserChangePasswordRequest userChangePasswordRequest = (UserChangePasswordRequest) request;
        assertEquals("PASSWORD", userChangePasswordRequest.getCommand());
        assertEquals("exampleUser", userChangePasswordRequest.getUserToUpdate());
        assertEquals("newPassword", userChangePasswordRequest.getNewPassword());
    }

    @Test
    @DisplayName("Should test UserRemoveRequest return")
    public void testGetUserRemoveRequest() throws IOException {
        when(mockClientConnection.isUserAuthorized()).thenReturn(true);
        when(mockUserInteraction.chooseUpdateOperation()).thenReturn("REMOVE");
        when(mockUserInteraction.chooseUserToUpdate()).thenReturn("exampleUser");

        Request request = factory.getUpdateRequest();

        UserRemoveRequest userRemoveRequest = (UserRemoveRequest) request;
        assertEquals("REMOVE", userRemoveRequest.getCommand());
        assertEquals("exampleUser", userRemoveRequest.getUserToUpdate());
    }

    @Test
    @DisplayName("Should test UserChangeRoleRequest return")
    public void testGetUserChangeRoleRequest() throws IOException {
        when(mockClientConnection.isUserAuthorized()).thenReturn(true);
        when(mockUserInteraction.chooseUpdateOperation()).thenReturn("ROLE");
        when(mockUserInteraction.chooseUserToUpdate()).thenReturn("exampleUser");
        when(mockUserInteraction.chooseRole()).thenReturn(User.Role.ADMIN);

        Request request = factory.getUpdateRequest();

        UserChangeRoleRequest userChangeRoleRequest = (UserChangeRoleRequest) request;
        assertEquals("ROLE", userChangeRoleRequest.getCommand());
        assertEquals("exampleUser", userChangeRoleRequest.getUserToUpdate());
        assertEquals(User.Role.ADMIN, userChangeRoleRequest.getNewRole());
    }

    @Test
    public void testGetUpdateRequestUnauthorized() throws IOException {
        when(mockClientConnection.isUserAuthorized()).thenReturn(false);

        Request request = factory.getUpdateRequest();

        assertNull(request);
    }
}