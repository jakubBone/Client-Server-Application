import org.junit.jupiter.api.*;
import server.ServerConnectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;
public class ServerConnectionHandlerTest {
    public ServerConnectionHandler handler;
    public ServerSocket mockServerSocket;
    public  Socket mockClientSocket;
    public BufferedReader mockInFromClient;
    public PrintWriter mockOutToClient;
    @BeforeEach
    public void setUp() throws IOException{
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
    public void closeDown() throws IOException {
        handler.closeConnections();

    }
    @Test
    @DisplayName("Should connect to server")
    public void testConnectWithClient() throws IOException {
        when(mockClientSocket.isConnected()).thenReturn(true);
        handler.startServer();
        Assertions.assertTrue(mockClientSocket.isConnected());
    }
}
