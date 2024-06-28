package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    @DisplayName("Should test changing user password")
    void testChangePassword() {
        String username = "exampleUsername";
        String password = "examplePassword";

        // Register user
        userManager.register(username, password);

        // Find the user and change the password
        User user = userManager.getUserByUsername(username);
        userManager.getAdmin().changePassword(user, "newPassword");

        assertNotEquals(user.getPassword(), password);
        assertEquals(user.getPassword(), "newPassword");
    }

    @Test
    @DisplayName("should test deleting user")
    void testDeleteUser() {
        String username = "exampleUsername";
        String password = "examplePassword";

        // Register user
        userManager.register(username, password);

        // Find the user and delete
        User user = userManager.getUserByUsername(username);
        userManager.getAdmin().deleteUser(user);

        assertNull(userManager.getUserByUsername(username));
    }

    @Test
    @DisplayName("should test user role changing")
    void testChangeUserRole() {
        String username = "exampleUsername";
        String password = "examplePassword";

        // Register user
        userManager.register(username, password);

        // Find the user change the role
        User user = userManager.getUserByUsername(username);
        userManager.getAdmin().changeUserRole(user, User.Role.ADMIN);

        userManager.login(user);
        assertTrue(userManager.isUserAdmin());

    }

    @Test
    @DisplayName("should test user switching")
    void testSwitchUser() {
        String username = "exampleUsername";
        String password = "examplePassword";

        // Register user
        userManager.register(username, password);

        // Find the user change the role
        User user = userManager.getUserByUsername(username);
        userManager.getAdmin().switchUser(user);

        assertTrue(UserManager.currentLoggedInUser.equals(user));
        assertTrue(UserManager.ifAdminSwitched);
    }
}