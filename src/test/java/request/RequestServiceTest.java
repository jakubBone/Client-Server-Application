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
public class RequestServiceTest {
    private RequestService requestService;
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
        requestService = new RequestService(clientConnection);
        factory = new RequestFactory();
        reader = new BufferedReader(new InputStreamReader(System.in));
        userManager = new UserManager();
        userInteraction = new UserInteraction(reader);
    }

    @Test
    @DisplayName("Should test request creating for logged user")
    void testGetRequest_Client_LoggedIn() throws IOException {
        String request = "REGISTER";
        Request expectedType = factory.createAuthRequest(request,
                exampleUser.getUsername(), "examplePassword");
        ClientConnection.loggedIn = false;

        // Test AuthRequest creating
        Request requestType = requestService.getRequest(request);

        assertNotNull(requestType);
        assertEquals(expectedType.getClass(), requestType.getClass());
    }

    @Test
    @DisplayName("Should test request creating for not logged user")
    void testCreateRequest_Client_LoggedOut() throws IOException {
        String request = "WRITE";
        Request expectedType = factory.createWriteRequest(request,
                "exampleUsername", "exampleMessage");
        ClientConnection.loggedIn = true;

        // Test WriteRequest creating
        Request requestType = requestService.getRequest(request);

        assertNotNull(requestType);
        assertEquals(expectedType.getClass(), requestType.getClass());
    }

    @Test
    @DisplayName("Should test account update request creating for non-admin user")
    void testGetAccountUpdateRequest_Not_Authorized() throws IOException {
        clientConnection.setAuthorized(false);

        // // Test AccountUpdateRequest getting
        Request requestType = requestService.getAccountUpdateRequest();

        assertNull(requestType);
    }
}