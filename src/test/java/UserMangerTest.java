import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;
import user.UserManager;

import static org.junit.jupiter.api.Assertions.*;

public class UserMangerTest {

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }
    @Test
    @DisplayName("Should test user registration")
    void testRegister() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        String registrationStatus = userManager.register(userName, password);

        assertEquals("User does not exist", registrationStatus);
    }

    @Test
    @DisplayName("Should test user registration with existing username")
    void testRegisterExistingUser() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        String registrationStatus = userManager.register(userName, password);

        assertEquals("User exist", registrationStatus);
    }

    @Test
    @DisplayName("Should test user login with correct credentials")
    void testLoginCorrect() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User loggedUser = userManager.login(userName, password);


        assertNotNull(loggedUser);
        assertEquals(userName, loggedUser.getUsername());
        assertEquals(password, loggedUser.getPassword());
    }

    @Test
    @DisplayName("Should test user login with incorrect credentials")
    void testLoginIncorrect() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User loggedUser = userManager.login(userName, "wrongPassword");

        assertNull(loggedUser);
    }

    @Test
    @DisplayName("Should test user logout ")
    void testLogout() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        userManager.login(userName, password);
        userManager.logoutCurrentUser();

        assertNull(UserManager.currentLoggedInUser);
    }

    @Test
    @DisplayName("should test finding recipient by username")
    void testGetRecipientByUsername() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User recipient = userManager.getRecipientByUsername(userName);

        assertNotNull(recipient);
        assertEquals(userName, recipient.getUsername());
    }

    @Test
    @DisplayName("should test finding user by username")
    void testFindUserByUsername() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User foundUser = userManager.findUserByUsername(userName);

        assertNotNull(foundUser);
        assertEquals(userName, foundUser.getUsername());
    }

    @Test
    @DisplayName("should test changing user password")
    void testChangePassword() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User user = userManager.findUserByUsername(userName);
        userManager.changePassword(user, "newPassword");

        assertFalse(user.getPassword().equals(password));
        assertTrue(user.getPassword().equals("newPassword"));
    }

    @Test
    @DisplayName("should test deleting user")
    void testDeleteUser() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        userManager.register(userName, password);
        User user = userManager.findUserByUsername(userName);
        userManager.deleteUser(user);

        assertNull(userManager.findUserByUsername(userName));
    }

    @Test
    @DisplayName("should test if current user is admin")
    void testIsAdmin() {
        String userName = "admin";
        String password = "java10";

        userManager.login(userName, password);
        assertTrue(userManager.isAdmin());
    }
}
