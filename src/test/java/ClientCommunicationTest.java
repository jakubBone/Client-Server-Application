import client.ClientConnection;
import operations.OperationResponses;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ClientCommunicationTest {

    private static ClientConnection clientConnection;
    private static Socket mockSocket;
    private static PrintWriter mockOutToServer;
    private static BufferedReader mockInFromServer;


    @BeforeEach
    public void setUp(){
        mockSocket = mock(Socket.class);
        mockOutToServer = mock(PrintWriter.class);
        mockInFromServer = mock(BufferedReader.class);
        clientConnection = new ClientConnection();
        clientConnection.setClientSocket(mockSocket);
        clientConnection.setOutToServer(mockOutToServer);
        clientConnection.setInFromServer(mockInFromServer);
    }

    @AfterAll
    @Test
    public static void closeDown() throws IOException {
        clientConnection.disconnect();
    }

    @Test
    @DisplayName("Should connect to server")
    public void testConnectToServer() throws IOException {
        Assertions.assertTrue(clientConnection.isConnected());
    }

    @Test
    @DisplayName("Should send request")
    public void testSendRequest() {
        String request = "exampleRequest";
        clientConnection.sendRequest(request);
        verify(mockOutToServer).println(request);
    }

    @Test
    @DisplayName("Should read response")
    void testReadResponse() throws IOException {
        String jsonResponse1 = "{\"message\":\"response1\"}";
        String jsonResponse2 = "{\"message\":\"response2\"}";

        when(mockInFromServer.readLine())
                .thenReturn(jsonResponse1, jsonResponse2, "<<END>>");

        clientConnection.readResponse();

        verify(mockInFromServer, times(3)).readLine();
    }

    @Test
    @DisplayName("Should check response status")
    public void testCheckResponseStatus() {
        String response = OperationResponses.LOGIN_SUCCESSFUL.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertTrue(clientConnection.isLoggedIn());

        response = OperationResponses.REGISTRATION_SUCCESSFUL.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertTrue(clientConnection.isLoggedIn());

        response = OperationResponses.SUCCESSFULLY_LOGGED_OUT.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertFalse(clientConnection.isLoggedIn());

        response = OperationResponses.REGISTRATION_FAILED.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertFalse(clientConnection.isLoggedIn());

        response = OperationResponses.OPERATION_SUCCEEDED.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertTrue(clientConnection.isAuthorized());

        response = OperationResponses.OPERATION_FAILED.getResponse();
        clientConnection.checkResponseStatus(response);
        Assertions.assertFalse(clientConnection.isAuthorized());
    }
}
