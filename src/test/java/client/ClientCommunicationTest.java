package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.*;
import shared.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCommunicationTest {
    static ClientConnection clientConnection;
    Socket mockSocket;
    PrintWriter mockOutToServer;
    BufferedReader mockInFromServer;

    @BeforeEach
    void setUp() {
        mockSocket = mock(Socket.class);
        mockOutToServer = mock(PrintWriter.class);
        mockInFromServer = mock(BufferedReader.class);
        clientConnection = new ClientConnection();
        clientConnection.setClientSocket(mockSocket);
        clientConnection.setOutToServer(mockOutToServer);
        clientConnection.setInFromServer(mockInFromServer);
    }

    @AfterAll
    static void closeDown() {
        clientConnection.disconnect();
    }

    @Test
    @DisplayName("Should test connecting to server")
    void testConnectToServer()  {
        Assertions.assertTrue(clientConnection.isConnected());
    }

    @Test
    @DisplayName("Should test sending request")
    void testSendRequest() {
        String request = "exampleRequest";

        clientConnection.sendRequest(request);

        // Verify that the request was sent to the server
        verify(mockOutToServer).println(request);
    }

    @Test
    @DisplayName("Should test checking response status")
    void testReadResponse() throws IOException {
        String jsonResponse1 = "{\"message\":\"response1\"}";
        String jsonResponse2 = "{\"message\":\"response2\"}";

        // Mock server responses
        when(mockInFromServer.readLine())
                .thenReturn(jsonResponse1, jsonResponse2, "<<END>>");

        clientConnection.readResponse();

        // Verify that the responses were read from the server
        verify(mockInFromServer, times(3)).readLine();
    }

    @Test
    @DisplayName("Should check response status")
    void testCheckResponseStatus() {
        // Test admin login succeeded
        String response = ResponseStatus.ADMIN_LOGIN_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isLoggedIn());
        assertTrue(clientConnection.isUserAuthorized());

        // Test user login succeeded
        response = ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isLoggedIn());

        // Test registration succeeded
        response = ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isLoggedIn());

        // Test logout succeeded
        response = ResponseStatus.LOGOUT_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertFalse(clientConnection.isLoggedIn());
        assertFalse(clientConnection.isUserAuthorized());

        // Test authorization succeeded
        response = ResponseStatus.AUTHORIZATION_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isUserAuthorized());

        // Test switch operation succeeded for admin
        response = ResponseStatus.SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isUserAuthorized());

        // Test switch operation succeeded for non-admin user
        response = ResponseStatus.SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE.getResponse();
        clientConnection.checkResponseStatus(response);
        assertFalse(clientConnection.isUserAuthorized());
    }
}