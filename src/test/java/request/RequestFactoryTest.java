package request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.AccountUpdateRequest;
import request.LoginRegisterRequest;
import request.Request;
import request.RequestFactory;
import user.User;
import user.UserManager;
import utils.UserInteraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

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

    @BeforeEach
    void setUp() {
        exampleUser = new User("exampleName", "examplePassword", User.Role.USER);
        UserManager.currentLoggedInUser = exampleUser;
        factory = new RequestFactory();
        reader = new BufferedReader(new InputStreamReader(System.in));
        userInteraction = new UserInteraction(reader);
    }

    @Test
    @DisplayName("Should test request creating")
    void testCreateRequest() throws IOException {
        String request = "REGISTER";
        Request expectedType = new LoginRegisterRequest(request,
                "exampleUser", "examplePassword");

        // Test creating a request
        Request requestType = factory.createRequest(request, userInteraction);

        assertNotNull(requestType);
        assertEquals(expectedType.getClass(), requestType.getClass());
    }

    @Test
    @DisplayName("Should test account update request creating")
    void testAccountUpdateRequest() throws IOException {
        String simulatedInput = "PASSWORD\nexampleUser\nnewPassword";
        reader = new BufferedReader(new StringReader(simulatedInput));
        userInteraction = new UserInteraction(reader);

        // Test creating an account update request
        Request reguest = factory.createAccountUpdateRequest(userInteraction);
        Request expectedRequest = new AccountUpdateRequest("PASSWORD",
                "exampleUser", "newPassword");

       assertEquals(expectedRequest.getUpdateOperation(), reguest.getUpdateOperation());
       assertEquals(expectedRequest.getUserToUpdate(), reguest.getUserToUpdate());
       assertEquals(expectedRequest.getNewPassword(), reguest.getNewPassword());
    }
}
