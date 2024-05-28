import client.Client;
import client.ClientConnection;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import request.Request;
import request.RequestFactory;
import utils.Screen;
import utils.UserInteraction;

import java.io.*;

import static org.mockito.Mockito.*;

class ClientTest {

    private Client client;
    private ClientConnection mockConnection;
    private BufferedReader mockUserInput;
    private UserInteraction mockUserInteraction;
    private RequestFactory mockFactory;
    private Request mockRequestType;

    @BeforeEach
    void setUp() {
        client = new Client();
        mockConnection = mock(ClientConnection.class);
        mockUserInput = mock(BufferedReader.class);
        client.setConnection(mockConnection);
        client.setUserInput(mockUserInput);
        mockUserInteraction = mock(UserInteraction.class);
        mockFactory = mock(RequestFactory.class);
        mockRequestType = mock(Request.class);
    }

    @AfterEach
    void closeDown() throws IOException {
        mockConnection.disconnect();
    }

    @Test
    @DisplayName("Should test disconnect when user inputs 'EXIT'")
    void testHandleServerCommunication_EXIT() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);
        when(mockUserInput.readLine()).thenReturn("EXIT");

        client.handleServerCommunication();

        Assertions.assertTrue(mockConnection.isConnected());
        Assertions.assertTrue(mockConnection.isLoggedIn());
        verify(mockConnection, times(1)).disconnect();
    }

    @Test
    @DisplayName("Should test communication handling when user is NOT LOGGED IN")
    void testHandleServerCommunication_NotLoggedIn() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(false);

        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        mockScreen.verify(() -> Screen.printLoginMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when user is LOGGED IN")
    void testHandleServerCommunication_LoggedIn() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);

        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        mockScreen.verify(() -> Screen.printMailBoxMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when user is NOT CONNECTED")
    void testHandleServerCommunication_NotConnected() throws IOException {
        when(!mockConnection.isConnected()).thenReturn(false);

        client.handleServerCommunication();

        verify(mockConnection, never()).isLoggedIn();
        verify(mockUserInput, never()).readLine();
    }

    @Test
    @DisplayName("Should test request handling")
    void testHandleRequest() {
        Assertions.fail("Not implemented");
    }
}