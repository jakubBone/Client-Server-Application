
import client.Client;
import client.ClientConnection;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import utils.Screen;

import java.io.*;

import static org.mockito.Mockito.*;

public class ClientTest {

    private Client mockClient;
    private ClientConnection mockConnection;
    private BufferedReader mockUserInput;
    private Logger mockLogger;
    private Screen mockScreen;


    @BeforeEach
    public void setUp() {
        mockClient = mock(Client.class);
        mockConnection = mock(ClientConnection.class);
        mockUserInput = mock(BufferedReader.class);
        mockLogger = mock(Logger.class);
        mockScreen = mock(Screen.class);
    }

    @AfterEach
    public void closeDown() throws IOException {
        mockConnection.disconnect();
        mockUserInput.close();
    }

    @Test
    @DisplayName("Should test handle disconnect when user inputs 'EXIT'")
    void testHandleServerCommunication_EXIT() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true, false);
        when(mockConnection.isLoggedIn()).thenReturn(true);
        when(mockUserInput.readLine()).thenReturn("EXIT");

        mockClient.handleServerCommunication();

        verify(mockConnection, times(1)).disconnect();
        verify(mockLogger).info("User exited the application");
    }

    @Test
    @DisplayName("Should test handle communication when user NOT LOGGED IN")
    void testHandleServerCommunication_NotLoggedIn() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true, false);
        when(mockConnection.isLoggedIn()).thenReturn(false);
        when(mockUserInput.readLine()).thenReturn("Request");

        mockClient.handleServerCommunication();

        verify(mockScreen.getClass());
    }
    @Test
    @DisplayName("Should test handle communication when user LOGGED IN")
    void testHandleServerCommunication_LoggedIn() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true, false);
        when(mockConnection.isLoggedIn()).thenReturn(true);
        when(mockUserInput.readLine()).thenReturn("Request");

        mockClient.handleServerCommunication();

        verify(mockScreen);
        Screen.printMailBoxMenu();
    }
    @Test
    @DisplayName("Should test handle communication when user NOT CONNECTED")
    void testHandleServerCommunication_NotConnected() throws IOException {
        when(mockConnection.isConnected()).thenReturn(false);

        mockClient.handleServerCommunication();

        verify(mockLogger).error("Error in handling server communication: Connection error");
    }
}

