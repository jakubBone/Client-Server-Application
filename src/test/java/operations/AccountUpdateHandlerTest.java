package operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.AdminChangePasswordRequest;
import request.Request;
import user.User;
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
    void testGetUpdateResponse() throws IOException {
        String userName = "admin";
        String password = "java10";
        String expectedStatus = "Operation succeeded: Authorized";
        String unexpectedStatus = "Operation failed: Not authorized";
        User admin = new User("admin", "java10", User.Role.ADMIN);

        // Simulate admin login
        userManager.login(admin);
        String status = updateHandler.changePassworAndGetResponse(admin.getUsername(), );

        assertEquals(expectedStatus, status);
        assertNotEquals(unexpectedStatus, status);
    }

    @Test
    @DisplayName("Should test getting update status for non-admin user")
    void testGetUpdateStatusNonAdmin() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedStatus = "Operation failed: Not authorized";
        String unexpectedStatus = "Operation succeeded: Authorized";

        // Register and log in a non-admin user
        userManager.register(userName, password);
        String status = updateHandler.changePassworAndGetResponse(userManager);

        assertEquals(expectedStatus, status);
        assertNotEquals(unexpectedStatus, status);
    }

    @Test
    @DisplayName("Should test getting update response for password change")
    void testGetUpdateResponsePassword() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedResponse = "exampleUsername password change successful";
        Request passwordChange = new AdminChangePasswordRequest("PASSWORD",
                userName, "newPassword");

        // Register and log in a user, then test password change request
        userManager.register(userName, password);
        String updateResponse = updateHandler.changePassworAndGetResponse(passwordChange, userManager);

        assertEquals(expectedResponse, updateResponse);
    }

    @Test
    @DisplayName("Should test getting update response for account deletion")
    void testGetUpdateResponseDELETE() throws IOException {
        String userName = "exampleUsername";
        String password = "examplePassword";
        String expectedResponse = "exampleUsername account deletion successful";
        Request passwordChange = new AdminChangePasswordRequest("DELETE",
                userName, "newPassword");

        // Register and log in a user, then test account deletion request
        userManager.register(userName, password);
        String updateResponse = updateHandler.changePassworAndGetResponse(passwordChange, userManager);

        assertEquals(expectedResponse, updateResponse);
    }
}