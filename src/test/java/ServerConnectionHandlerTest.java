import org.junit.jupiter.api.*;
import server.ServerConnectionHandler;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;
class ServerConnectionHandlerTest {
    private ServerConnectionHandler handler;
    private ServerSocket mockServerSocket;
    private Socket mockClientSocket;
    private BufferedReader mockInFromClient;
    private PrintWriter mockOutToClient;
    @BeforeEach
    void setUp()  {
        handler = new ServerConnectionHandler(5000);
        mockServerSocket = mock(ServerSocket.class);
        mockClientSocket = mock(Socket.class);
        mockInFromClient = mock(BufferedReader.class);
        mockOutToClient = mock(PrintWriter.class);
        handler.setClientSocket(mockClientSocket);
        handler.setServerSocket(mockServerSocket);
        handler.setOutToClient(mockOutToClient);
        handler.setInFromClient(mockInFromClient);
    }

    @AfterEach
    void closeDown()  {
        handler.closeConnections();

    }

    @Test
    @DisplayName("Should connect to server")
    void testConnectWithClient()  {
        when(mockClientSocket.isConnected()).thenReturn(true);
        handler.startServer();
        Assertions.assertTrue(mockClientSocket.isConnected());
    }
}
