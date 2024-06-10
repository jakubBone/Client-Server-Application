package handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shared.OperationResponses;
import user.Admin;
import user.User;
import user.UserManager;

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
    @DisplayName("Should test getting update response for password change")
    void testGetPasswordChangeResponse() {
        Admin admin = new Admin();

        // Admin login
        userManager.login(admin);
        // Password changing
        String response = updateHandler.getChangePasswordResponse(admin.getUsername(), "newPasswod", userManager);

        assertNotEquals(OperationResponses.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(OperationResponses.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(OperationResponses.OPERATION_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test getting update status for user account deletion")
    void testGetUserDeleteResponse() {
        String userName = "exampleUsername";
        String password = "examplePassword";
        Admin admin = new Admin();

        // User register
        userManager.register(userName, password);
        // Admin login
        userManager.login(admin);
        // User account deletion
        String response = updateHandler.getUserDeleteResponse(userName, userManager);

        assertNotEquals(OperationResponses.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(OperationResponses.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(OperationResponses.OPERATION_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test getting update response for user role change")
    void testGetChangeRoleResponse() {
        Admin admin = new Admin();

        // Admin login
        userManager.login(admin);
        // Role changing
        String response = updateHandler.getChangeRoleResponse(admin.getUsername(), User.Role.USER, userManager);

        assertNotEquals(OperationResponses.AUTHORIZATION_FAILED.getResponse(), response);
        assertNotEquals(OperationResponses.FAILED_TO_FIND_USER.getResponse(), response);
        assertEquals(OperationResponses.ROLE_CHANGE_SUCCEEDED.getResponse(), response);
    }
}