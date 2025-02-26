package unit_test.application;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.domain.User;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    SessionManager sessionManager;
    UserService userService;
    UserRepository userRepository;
    AuthService authService;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);
        authService = new AuthService(sessionManager, userRepository);
        when(userService.getUserRepository()).thenReturn(userRepository);
    }

    @Test
    @DisplayName("Should test registration of a new user")
    void testRegisterNewUserSuccess() {
        String username = "newUser";
        String password = "pass123";

        // User does not exist
        when(userRepository.findUserByUsername(username)).thenReturn(null);

        String response = authService.register(username, password);

        verify(userRepository, times(1)).createUser(any(User.class));
        assertEquals(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse(), response);
    }

    @Test
    @DisplayName("Should test registration with already existing user")
    void testRegisterUserAlreadyExistsFailure() {
        String username = "existingUser";
        String password = "pass123";
        User existingUser = new User(username, password, User.Role.USER);

        when(userRepository.findUserByUsername(username)).thenReturn(existingUser);

        String response = authService.register(username, password);

        // New user creation should not occur
        verify(userRepository, never()).createUser(any(User.class));
        assertEquals(ResponseStatus.REGISTRATION_FAILED_USER_EXISTS.getResponse(), response);
    }

    @Test
    @DisplayName("Should test login with a non-existent user")
    void testLoginUserNotFoundFailure() {
        String username = "nonexistent";
        String password = "pass123";

        when(userRepository.findUserByUsername(username)).thenReturn(null);

        String response = authService.login(username, password);

        assertEquals(ResponseStatus.FAILED_TO_FIND_USER.getResponse(), response);
        assertNull(sessionManager.getCurrentUser());
    }

    @Test
    @DisplayName("Should test login with an incorrect password")
    void testLoginIncorrectPasswordFailure() {
        String username = "user";
        String password = "wrongPass";
        User user = new User(username, "correctPassword", User.Role.USER);

        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(userRepository.verifyUserPassword(password, username)).thenReturn(false);

        String response = authService.login(username, password);

        assertEquals(ResponseStatus.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse(), response);
        assertNull(sessionManager.getCurrentUser());
    }

    @Test
    @DisplayName("Should test successful login for a regular user")
    void testLoginCorrectPasswordUserSuccess() {
        String username = "user";
        String password = "correctPassword";
        User user = new User(username, password, User.Role.USER);

        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(userRepository.verifyUserPassword(password, username)).thenReturn(true);

        String response = authService.login(username, password);

        // The user should be set in the session
        assertEquals(user, sessionManager.getCurrentUser());
        assertEquals(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test successful login for an admin")
    void testLoginCorrectPasswordAdminSuccess() {
        String username = "adminUser";
        String password = "adminPass";
        User admin = new User(username, password, User.Role.ADMIN);

        when(userRepository.findUserByUsername(username)).thenReturn(admin);
        when(userRepository.verifyUserPassword(password, username)).thenReturn(true);

        String response = authService.login(username, password);

        assertEquals(admin, sessionManager.getCurrentUser());
        assertEquals(ResponseStatus.ADMIN_LOGIN_SUCCEEDED.getResponse(), response);
    }

    @Test
    @DisplayName("Should test password checking correctness")
    void testIsPasswordCorrect() {
        String username = "testUser";
        String password = "pass123";
        User user = new User(username, password, User.Role.USER);

        when(userRepository.verifyUserPassword(password, username)).thenReturn(true);

        boolean result = authService.isPasswordCorrect(password, user);
        assertTrue(result);

        when(userRepository.verifyUserPassword("wrong", username)).thenReturn(false);
        result = authService.isPasswordCorrect("wrong", user);
        assertFalse(result);
    }

    @Test
    @DisplayName("Should test user logout")
    void testLogout() {
        User user = new User("user", "pass", User.Role.USER);
        sessionManager.setCurrentUser(user);

        String response = authService.logout();

        assertNull(sessionManager.getCurrentUser());
        assertEquals(ResponseStatus.LOGOUT_SUCCEEDED.getResponse(), response);
    }
}


