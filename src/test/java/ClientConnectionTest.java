import client.ClientConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientConnectionTest {
    @Mock
    private Socket mockSocket;
    @Mock
    private PrintWriter mockOutToServer;
    @Mock
    private BufferedReader mockInFromServer;

    @InjectMocks
    private ClientConnection clientConnection;

    @BeforeEach
    void setupConnection() throws IOException {
        when(mockSocket.getOutputStream()).thenReturn(System.out);
        when(mockSocket.getInputStream()).thenReturn(System.in);

        clientConnection = new ClientConnection();
        clientConnection.setClientSocket(mockSocket);
        clientConnection.setOutToServer(mockOutToServer);
        clientConnection.setInFromServer(mockInFromServer);
    }

    @Test
    @DisplayName("Client connection init testing")
    void testClientConnectionInit() throws IOException {
        when(new Socket("local host", 5000));
        clientConnection.connectToServer();

        assertNotNull(clientConnection.getClientSocket());
        assertNotNull(clientConnection.getOutToServer());
        assertNotNull(clientConnection.getInFromServer());
        verify(mockSocket,times(1)).getOutputStream();
        verify(mockSocket,times(1)).getInputStream();

    }
}
