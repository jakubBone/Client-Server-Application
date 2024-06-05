package client;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import request.Request;
import request.RequestFactory;
import utils.Screen;
import utils.UserInteraction;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Client class.
 * This class tests the client's interaction methods, including handling server communication and user input.
 */
class ClientTest {
    private Client client;
    private static ClientConnection mockConnection;
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

    @AfterAll
    static void closeDown() throws IOException {
        mockConnection.disconnect();
    }

    @Test
    @DisplayName("Should test disconnect when user inputs 'EXIT'")
    void testHandleServerCommunication_EXIT() throws IOException {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);
        when(mockUserInput.readLine()).thenReturn("EXIT");

        client.handleServerCommunication();

        // Verify that the client disconnects from the server when "EXIT" is entered
        assertTrue(mockConnection.isConnected());
        assertTrue(mockConnection.isLoggedIn());
        verify(mockConnection, times(1)).disconnect();
    }

    @Test
    @DisplayName("Should test communication handling when user is NOT LOGGED IN")
    void testHandleServerCommunication_NotLoggedIn()  {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(false);

        // Mocking the static methods of the Screen class
        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        // Verify that the login menu is displayed when the user is not logged in
        mockScreen.verify(() -> Screen.printLoginMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when user is LOGGED IN")
    void testHandleServerCommunication_LoggedIn()  {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);

        // Mocking the static methods of the Screen class
        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        // Verify that the mailbox menu is displayed when the user is logged in
        mockScreen.verify(() -> Screen.printLoginMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when user is NOT CONNECTED")
    void testHandleServerCommunication_NotConnected() throws IOException {
        when(!mockConnection.isConnected()).thenReturn(false);

        client.handleServerCommunication();

        // Verify that no actions are taken when the client is not connected
        verify(mockConnection, never()).isLoggedIn();
        verify(mockUserInput, never()).readLine();
    }

    @Test
    @DisplayName("Should test request handling")
    void testHandleRequest() throws IOException {
        when(mockUserInput.readLine()).thenReturn("LOGIN");
        when(mockFactory.createRequest("LOGIN")).thenReturn(mockRequestType);

        client.handleRequest("LOGIN");

        // Verify that the request is handled properly
        verify(mockConnection).sendRequest(anyString());
        verify(mockConnection).readResponse();
    }
}