package operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.AccountUpdateRequest;
import request.Request;
import user.UserManager;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AccountUpdateHandler class.
 * This class tests the handling of user account updates, ensuring authorization and correct processing of requests.
 */
public class AccountUpdateHandlerTest {
    private AccountUpdateHandler updateHandler;
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        updateHandler = new AccountUpdateHandler();
        userManager = new UserManager();
    }

    @Test
    @DisplayName("Should test getting update status for admin user")
    void testGetUpdateStatusAdmin() throws IOException {
        String userName = "admin";
        String password = "java10";
        String expectedStatus = "Operation succeeded: Authorized";
        String unexpectedStatus = "Operation failed: Not authorized";

        // Simulate admin login
        userManager.login(userName, password);
        String status = updateHandler.getUpdateStatus(userManager);

        assertEquals(expectedStatus, status);
        assertNotEquals(unexpectedStatus, status);
    }

    @Test
    @DisplayName("Should test getting update status for non-admin user")
    void testGetsUpdateStatusNonAdmin() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedStatus = "Operation failed: Not authorized";
        String unexpectedStatus = "Operation succeeded: Authorized";

        // Register and log in a non-admin user
        userManager.register(userName, password);
        userManager.login(userName, password);
        String status = updateHandler.getUpdateStatus(userManager);

        assertEquals(expectedStatus, status);
        assertNotEquals(unexpectedStatus, status);
    }

    @Test
    @DisplayName("Should test getting update response for password change")
    void testGetsUpdateResponsePassword() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedResponse = "exampleUsername password change successful";
        Request passwordChange = new AccountUpdateRequest("PASSWORD",
                userName, "newPassword");

        // Register and log in a user, then test password change request
        userManager.register(userName, password);
        userManager.login(userName, password);
        String updateResponse = updateHandler.getUpdateResponse(passwordChange, userManager);

        assertEquals(expectedResponse, updateResponse);
    }

    @Test
    @DisplayName("Should test getting update response for account deletion")
    void testGetsUpdateResponseDELETE() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedResponse = "exampleUsername password change successful";
        Request passwordChange = new AccountUpdateRequest("DELETE",
                userName, "newPassword");

        // Register and log in a user, then test account deletion request
        userManager.register(userName, password);
        userManager.login(userName, password);
        String updateResponse = updateHandler.getUpdateResponse(passwordChange, userManager);

        assertEquals(expectedResponse, updateResponse);
    }
}