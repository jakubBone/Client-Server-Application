package unit_test.application;

import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.UserService;
import com.jakub.bone.domain.User;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        userRepository = mock(UserRepository.class);
        authService = new AuthService(sessionManager, userRepository);
        userService = spy(new UserService(authService, sessionManager, userRepository));
        // Replace the repository with the mock
        doReturn(userRepository).when(userService).getUserRepository();
    }

    @Test
    @DisplayName("Finding an existing user")
    void findUserByUsername_Found() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);
        when(userRepository.findUserByUsername(username)).thenReturn(user);

        User found = userService.findUserByUsername(username);
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("Finding a non-existent user")
    void findUserByUsername_NotFound() {
        String username = "nonexistent";
        when(userRepository.findUserByUsername(username)).thenReturn(null);

        User found = userService.findUserByUsername(username);
        assertNull(found);
    }

    @Test
    @DisplayName("Changing a user's password")
    void changePassword_Test() {
        String username = "user";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        User user = new User(username, oldPassword, User.Role.USER);

        // Call the changePassword method
        userService.changePassword(user, newPassword);

        // Check that the password is updated (and hash is regenerated)
        assertEquals(newPassword, user.getPassword());
        assertNotNull(user.getHashedPassword());
        // Verify that repository update was invoked
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    @DisplayName("Removing a user")
    void removeUser_Test() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);

        userService.removeUser(user);

        verify(userRepository, times(1)).removeUser(username);
    }

    @Test
    @DisplayName("Switching user")
    void switchUser_Test() {
        User user = new User("user", "pass", User.Role.USER);
        // Initially, session should have no current user
        assertNull(sessionManager.getCurrentUser());

        userService.switchUser(user);

        assertEquals(user, sessionManager.getCurrentUser());
    }

    @Test
    @DisplayName("Changing user role")
    void changeUserRole_Test() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);

        // Change role to ADMIN
        userService.changeUserRole(user, User.Role.ADMIN);

        // Verify the role has changed
        assertEquals(User.Role.ADMIN, user.getRole());
        // Verify that repository update was called
        verify(userRepository, times(1)).changeUserRole(user, User.Role.ADMIN);
    }
}
