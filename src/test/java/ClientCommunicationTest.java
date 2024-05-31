import client.ClientConnection;
import operations.OperationResponses;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClientConnection class.
 * This class tests the client's ability to connect to the server,s end requests, read responses, and handle connection status.
 */
class ClientCommunicationTest {

    private static ClientConnection clientConnection;
    private Socket mockSocket;
    private PrintWriter mockOutToServer;
    private BufferedReader mockInFromServer;


    @BeforeEach
    void setUp(){
        mockSocket = mock(Socket.class);
        mockOutToServer = mock(PrintWriter.class);
        mockInFromServer = mock(BufferedReader.class);
        clientConnection = spy(new ClientConnection());
        clientConnection.setClientSocket(mockSocket);
        clientConnection.setOutToServer(mockOutToServer);
        clientConnection.setInFromServer(mockInFromServer);
    }

    @AfterAll
    static void closeDown()  {
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
        // Test login successful response
        String response = OperationResponses.LOGIN_SUCCESSFUL.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isLoggedIn());

        // Test registration successful response
        response = OperationResponses.REGISTRATION_SUCCESSFUL.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isLoggedIn());

        // Test successfully logged out response
        response = OperationResponses.SUCCESSFULLY_LOGGED_OUT.getResponse();
        clientConnection.checkResponseStatus(response);
        assertFalse(clientConnection.isLoggedIn());

        // Test registration failed response
        response = OperationResponses.REGISTRATION_FAILED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertFalse(clientConnection.isLoggedIn());

        // Test operation succeeded response
        response = OperationResponses.OPERATION_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertTrue(clientConnection.isAuthorized());

        // Test operation failed response
        response = OperationResponses.OPERATION_FAILED.getResponse();
        clientConnection.checkResponseStatus(response);
        assertFalse(clientConnection.isAuthorized());
    }
}
