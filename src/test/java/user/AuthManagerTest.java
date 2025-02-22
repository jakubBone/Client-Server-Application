package user;

import com.jakub.bone.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jakub.bone.application.service.AuthService;
import com.jakub.bone.application.service.UserService;
import com.jakub.bone.utils.ResponseStatus;
import com.jakub.bone.domain.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthManagerTest {
    UserService userManager;
    AuthService authManager;
    User user;
    UserRepository mockUserDAO;
    String username = "testUsername";
    String password = "testPassword";

    @BeforeEach
    void setUp() {
        userManager = new UserService();
        authManager = new AuthService();
        user = new User(username, password, User.Role.USER);
        mockUserDAO = mock(UserRepository.class);
        userManager.setUserDAO(mockUserDAO);
    }

    @Test
    @DisplayName("Should test user registration response with non-existent user in database")
    void testRegisterAndGetResponse() {
        when(mockUserDAO.getUserFromDB(username)).thenReturn(null);

        String response = authManager.registerAndGetResponse(username, password, userManager);

        assertEquals(ResponseStatus.REGISTRATION_SUCCESSFUL.getResponse(), response);
    }

    @Test
    @DisplayName("Should test user registration response with existing user in database")
    void testRegisterAndGetResponse_UserExists() {
        when(mockUserDAO.getUserFromDB(username)).thenReturn(user);

        String response = authManager.registerAndGetResponse(username, password, userManager);

        assertEquals(ResponseStatus.REGISTRATION_FAILED_USER_EXISTS.getResponse(), response);
    }

    @Test
    @DisplayName("Should test user login response with existing user in database")
    void testLoginAndGetResponse() {
        when(mockUserDAO.getUserFromDB(username)).thenReturn(user);
        when(mockUserDAO.checkPasswordInDB(password, username)).thenReturn(true);

        String response = authManager.loginAndGetResponse(username, password, userManager);

        assertEquals(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse(), response);
        assertEquals(user, UserService.currentLoggedInUser);
    }

    @Test
    @DisplayName("Should test user login response with incorrect password in database")
    void testLoginAndGetResponse_IncorrectPassword() {
        when(mockUserDAO.getUserFromDB(username)).thenReturn(user);
        when(mockUserDAO.checkPasswordInDB(username,password)).thenReturn(false);

        String response = authManager.loginAndGetResponse(username, password, userManager);

        assertEquals(ResponseStatus.LOGIN_FAILED_INCORRECT_PASSWORD.getResponse(), response);
        assertNotEquals(user, UserService.currentLoggedInUser);
    }

    @Test
    @DisplayName("Should test user registration handling")
    void testHandleRegister() {
        doNothing().when(mockUserDAO).addUserToDB(user);
        when(mockUserDAO.getUserFromDB(username)).thenReturn(user);

        authManager.handleRegister(username, password, userManager);

        User userFromDB = userManager.getUserByUsername(username);
        assertEquals(user, userFromDB);
    }
    @Test
    @DisplayName("Should test user registration handling")
    void testHandleLogin() {
        authManager.handleLogin(user);

        assertFalse(!UserService.currentLoggedInUser.equals(user));
        assertTrue(UserService.currentLoggedInUser.equals(user));
    }

    @Test
    @DisplayName("Should test user registration handling")
    void testIfPasswordCorrect() {
        when(mockUserDAO.checkPasswordInDB(password, username)).thenReturn(true);

        boolean isCorrect = authManager.isPasswordCorrect(password, user, userManager);

        assertFalse(!isCorrect);
        assertTrue(isCorrect);
    }
}


