package client;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import request.Request;
import request.RequestFactory;
import shared.Screen;
import shared.UserInput;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static shared.Screen.printAdminMailBoxMenu;

class ClientTest {
    Client client;
    static ClientConnection mockConnection;
    BufferedReader mockUserInput;
    UserInput mockUserInput;
    RequestFactory mockRequestFactory;
    Request mockRequestType;

    @BeforeEach
    void setUp() {
        client = new Client();
        mockConnection = mock(ClientConnection.class);
        mockUserInput = mock(BufferedReader.class);
        client.setConnection(mockConnection);
        client.setUserInput(mockUserInput);
        mockUserInput = mock(UserInput.class);
        mockRequestFactory = mock(RequestFactory.class);
        mockRequestType = mock(Request.class);
    }

    @AfterAll
    static void closeDown() {
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
    @DisplayName("Should test communication handling when admin user is LOGGED IN")
    void testHandleServerCommunication_LoggedIn_Authorized() {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);
        when(mockConnection.isUserAuthorized()).thenReturn(true);

        // Mocking the static methods of the Screen class
        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        // Verify that the mailbox menu is displayed when the user is logged in
        mockScreen.verify(() -> printAdminMailBoxMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when non-admin user is LOGGED IN")
    void testHandleServerCommunication_LoggedIn_Not_Authorized() {
        when(mockConnection.isConnected()).thenReturn(true);
        when(mockConnection.isLoggedIn()).thenReturn(true);

        // Mocking the static methods of the Screen class
        MockedStatic<Screen> mockScreen = mockStatic(Screen.class);
        client.handleServerCommunication();

        // Verify that the mailbox menu is displayed when the user is logged in
        mockScreen.verify(() -> Screen.printUserMailBoxMenu());
        mockScreen.close();
    }

    @Test
    @DisplayName("Should test communication handling when user is NOT LOGGED IN")
    void testHandleServerCommunication_NotLoggedIn() {
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
        when(mockRequestFactory.getRequest("LOGIN")).thenReturn(mockRequestType);

        client.handleRequest("LOGIN");

        // Verify that the request is handled properly
        verify(mockConnection).sendRequest(anyString());
        verify(mockConnection).readResponse();
    }
}