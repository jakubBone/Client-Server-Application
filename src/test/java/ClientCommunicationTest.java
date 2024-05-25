import client.ClientConnection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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


    // Need to be improved
    @ParameterizedTest
    @DisplayName("Should check response status")
    @CsvSource({
            "'Login successful', true, false",
            "'Registration successful', true, false",
            "'Successfully logged out', false, false",
            "'Login failed: Incorrect username or password', false, false",
            "'Registration failed', false, false",
            "'Operation succeeded: Authorized', false, true",
            "'Operation failed: Not authorized', false, false"
    })
    public void testCheckResponseStatus() {
        Assertions.fail("Not implemented yet");
    }

}
