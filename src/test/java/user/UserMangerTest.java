package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMangerTest {
    UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }
    @Test
    @DisplayName("Should test user registration for the first time")
    void testRegister() {
        String userName = "exampleUsername";
        String password = "examplePassword";
        User newUser = new User(userName, password, User.Role.USER);

        // Test user registration with a new username
        userManager.register(userName, password);
        assertEquals(UserManager.currentLoggedInUser.getUsername(), userName);
        assertEquals(UserManager.currentLoggedInUser.getPassword(), password);
        assertEquals(UserManager.currentLoggedInUser.role, User.Role.USER);
    }

    @Test
    @DisplayName("Should test user registration with existing username")
    void testRegisterExistingUser() {
        String userName = "exampleUsername";
        String password = "examplePassword";

        // Register the user for the first time
        userManager.register(userName, password);

        // Attempt to register the same user again
        userManager.register(userName, password);
        String registrationStatus = userManager.registerAndGetResponse(userName, password);

        assertEquals("Registration failed: User already exists", registrationStatus);
    }

    @Test
    @DisplayName("Should test user login with correct credentials")
    void testLoginCorrect() {
        String username = "exampleUsername";
        String password = "examplePassword";
        User newUser = new User(username, password, User.Role.USER);

        // Register the user first
        userManager.register(username, password);

        // Attempt to log in with correct credentials
        userManager.login(newUser);


        assertNotNull(UserManager.currentLoggedInUser);
        assertEquals(username, UserManager.currentLoggedInUser.getUsername());
        assertEquals(password, UserManager.currentLoggedInUser.getPassword());
    }

    @Test
    @DisplayName("Should test user login with incorrect credentials")
    void testLoginIncorrect() {
        String username = "exampleUsername";
        String password = "examplePassword";
        User anotherUser = new User(username,"wrongPassword" , User.Role.USER);

        // Register the user first
        userManager.register(username, password);

        // Attempt to log in with incorrect password
        userManager.login(anotherUser);
        String loginStatus = userManager.loginAndGetResponse(username, "wrongPassword");
        assertEquals("Login failed: Incorrect password", loginStatus);
    }

    @Test
    @DisplayName("Should test user logout")
    void testLogout() {
        String username = "exampleUsername";
        String password = "examplePassword";
        User user = new User(username,password , User.Role.USER);

        // Register and login the user first
        userManager.register(username, password);
        userManager.login(user);

        // Logout the current user
        userManager.logoutCurrentUser();

        assertNull(UserManager.currentLoggedInUser);
    }

    @Test
    @DisplayName("Should test finding user by username")
    void testFindUserByUsername() {
        String username = "exampleUsername";
        String password = "examplePassword";

        // Register the user first
        userManager.register(username, password);

        // Find the user by username
        User foundUser = userManager.getUserByUsername(username);

        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    @DisplayName("Should test if current user is admin")
    void testIfCurrentUserAdmin() {
        User admin = new Admin(userManager.DATABASE, userManager.CREATE);

        // Log in as the admin user
        userManager.login(admin);

        assertTrue(userManager.ifCurrentUserAdmin());
    }
}
