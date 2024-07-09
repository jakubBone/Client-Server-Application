package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerConnectionTest {
    ServerConnection handler;
    ServerSocket mockServerSocket;
    Socket mockClientSocket;
    BufferedReader mockInFromClient;
    PrintWriter mockOutToClient;

    @BeforeEach
    void setUp() {
        handler = new ServerConnection(5000);
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
    @DisplayName("Should test connecting to server")
    void testConnectWithClient() {
        when(mockClientSocket.isConnected()).thenReturn(true);
        handler.startServer();

        assertTrue(mockClientSocket.isConnected());
    }
}