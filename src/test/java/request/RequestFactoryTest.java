package request;

import client.ClientConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserManager;
import shared.UserInteraction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RequestFactory class.
 * This class tests the creation of various request objects based on user input.
 */
public class RequestFactoryTest {
    private RequestFactory factory;
    private UserInteraction userInteraction;
    private BufferedReader reader;
    private User exampleUser;
    private UserManager userManager;
    ClientConnection clientConnection;

    @BeforeEach
    void setUp() {
        exampleUser = new User("exampleName", "examplePassword", User.Role.USER);
        UserManager.currentLoggedInUser = exampleUser;
        clientConnection = new ClientConnection();
        factory = new RequestFactory(clientConnection);
        reader = new BufferedReader(new InputStreamReader(System.in));
        userManager = new UserManager();
        userInteraction = new UserInteraction(reader);
    }

    @Test
    @DisplayName("Should test request creating for logged user")
    void testCreateRequest_Client_LoggedIn() throws IOException {
        String request = "REGISTER";
        Request expectedType = new AuthRequest(request,
                exampleUser.getUsername(), "examplePassword");
        ClientConnection.loggedIn = false;

        // Test AuthRequest creating
        Request requestType = factory.createRequest(request);

        assertNotNull(requestType);
        assertEquals(expectedType.getClass(), requestType.getClass());
    }

    @Test
    @DisplayName("Should test request creating for not logged user")
    void testCreateRequest_Client_LoggedOut() throws IOException {
        String request = "WRITE";
        Request expectedType = new WriteRequest(request,
                "exampleUsername", "exampleMessage");
        ClientConnection.loggedIn = true;

        // Test WriteRequest creating
        Request requestType = factory.createRequest(request);

        assertNotNull(requestType);
        assertEquals(expectedType.getClass(), requestType.getClass());
    }

    @Test
    @DisplayName("Should test account update request creating for non-admin user")
    void testGetAccountUpdateRequest_Not_Authorized() throws IOException {
        clientConnection.setAuthorized(false);

        // // Test AccountUpdateRequest getting
        Request requestType = factory.getAccountUpdateRequest();

        assertNull(requestType);
    }
}