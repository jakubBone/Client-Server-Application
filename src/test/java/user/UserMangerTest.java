package user;

import com.jakub.bone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jakub.bone.application.service.UserService;
import com.jakub.bone.utils.ResponseStatus;
import com.jakub.bone.domain.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMangerTest {
    UserService userManager;
    User user;
    UserRepository mockUserDAO;
    String username = "testUsername";
    String password = "testPassword";

    @BeforeEach
    void setUp() {
        userManager = new UserService();
        user = new User(username, password, User.Role.USER);
        mockUserDAO = mock(UserRepository.class);
        userManager.setUserDAO(mockUserDAO);
    }

    @Test
    @DisplayName("Should test user registration for the first time")
    void testGetUserByUsername() {
        when(mockUserDAO.getUserFromDB("testUser")).thenReturn(user);

        User foundUser = userManager.getUserByUsername("testUser");
        assertEquals(user, foundUser);
    }

    @Test
    @DisplayName("Should test user registration for the first time")
    void testPasswordChange() {
        String newPassword = "newPassword";
        when(mockUserDAO.getUserFromDB(password)).thenReturn(user);

        userManager.changePassword(user, newPassword);

        assertNotEquals(password, user.getPassword());
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("Should test user deletion")
    void testDeleteUser() {
        doNothing().when(mockUserDAO).removeUserFromDB(username);

        userManager.removeUser(user);

        User foundUser = userManager.getUserByUsername(username);
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Should test user switch by admin")
    void testSwitchUser() {
        userManager.switchUser(user);

        assertEquals(user, UserService.currentLoggedInUser);
        assertTrue(UserService.ifSwitchedToNonAdminUser);
    }

    @Test
    @DisplayName("Should test user role change")
    void testChangeRole() {
        when(mockUserDAO.getUserFromDB(username)).thenReturn(user);

        userManager.changeUserRole(user, User.Role.ADMIN);

        assertEquals(User.Role.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Should test user switch by admin")
    void testLogoutAndGetResponse(){
        String response = userManager.logoutAndGetResponse();

        assertNull(UserService.currentLoggedInUser);
        assertFalse(UserService.ifSwitchedToNonAdminUser);
        assertEquals(ResponseStatus.LOGOUT_SUCCEEDED.getResponse(), response);
    }
}