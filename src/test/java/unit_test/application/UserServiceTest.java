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
    UserService userService;
    UserRepository mockUserRepository;
    SessionManager sessionManager;
    AuthService authService;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        mockUserRepository = mock(UserRepository.class);
        authService = new AuthService(sessionManager, mockUserRepository);
        userService = spy(new UserService(authService, sessionManager, mockUserRepository));
        doReturn(mockUserRepository).when(userService).getUserRepository();
    }

    @Test
    @DisplayName("Should test finding an existing user")
    void testFindUserByUsernameFound() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);
        when(mockUserRepository.findUserByUsername(username)).thenReturn(user);

        User found = userService.findUserByUsername(username);
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    @DisplayName("Should test finding a non-existent user")
    void testFindUserByUsernameNotFound() {
        String username = "nonexistent";
        when(mockUserRepository.findUserByUsername(username)).thenReturn(null);

        User found = userService.findUserByUsername(username);
        assertNull(found);
    }

    @Test
    @DisplayName("Should test changing password")
    void testChangePassword() {
        String username = "user";
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        User user = new User(username, oldPassword, User.Role.USER);

        userService.changePassword(user, newPassword);

        // Check that the password is updated and hash is regenerated
        assertEquals(newPassword, user.getPassword());
        assertNotNull(user.getHashedPassword());
        verify(mockUserRepository, times(1)).updateUser(user);
    }

    @Test
    @DisplayName("Should test removing user")
    void testRemoveUser() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);

        userService.removeUser(user);

        verify(mockUserRepository, times(1)).removeUser(username);
    }

    @Test
    @DisplayName("Should test switching user")
    void testSwitchUser() {
        User user = new User("user", "pass", User.Role.USER);
        // Initially, session should have no current user
        assertNull(sessionManager.getCurrentUser());

        userService.switchUser(user);

        assertEquals(user, sessionManager.getCurrentUser());
    }

    @Test
    @DisplayName("Should test changing user role")
    void testChangeUserRole() {
        String username = "user";
        User user = new User(username, "pass", User.Role.USER);

        // Change role to ADMIN
        userService.changeUserRole(user, User.Role.ADMIN);

        assertEquals(User.Role.ADMIN, user.getRole());
        verify(mockUserRepository, times(1)).changeUserRole(user, User.Role.ADMIN);
    }
}
