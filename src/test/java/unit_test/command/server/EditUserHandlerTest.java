package unit_test.command.server;

import com.jakub.bone.application.UserService;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.EditUserHandler;
import com.jakub.bone.domain.User;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class EditUserHandlerTest {
    UserService mockUserService;
    EditUserHandler editUserHandler;
    CommandDTO commandDTO;
    User dummyUser;

    @BeforeEach
    void setUp() {
        mockUserService = mock(UserService.class);
        editUserHandler = new EditUserHandler(mockUserService);
        dummyUser = new User("user", "pass", User.Role.USER);
        when(mockUserService.findUserByUsername("user")).thenReturn(dummyUser);
    }

    @Test
    @DisplayName("Should test EditUserHandler with CHANGE subcommand")
    void testChangePassword() {
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("subCommand", "CHANGE");
        payLoad.put("username", "user");
        payLoad.put("newPassword", "newPass");

        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(payLoad)
                .build();

        String expected = ResponseStatus.OPERATION_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(mockUserService, times(1)).changePassword(dummyUser, "newPass");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test EditUserHandler with ASSIGN subcommand")
    void testAssignRole() {
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("subCommand", "ASSIGN");
        payLoad.put("username", "user");
        payLoad.put("newRole", "ADMIN");

        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(payLoad)
                .build();

        String expected = ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(mockUserService, times(1)).changeUserRole(dummyUser, User.Role.ADMIN);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test EditUserHandler with REMOVE subcommand")
    void testRemoveUser() {
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("subCommand", "REMOVE");
        payLoad.put("username", "user");

        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(payLoad)
                .build();

        String expected = ResponseStatus.USER_DELETE_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(mockUserService, times(1)).removeUser(dummyUser);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test EditUserHandler with SWITCH subcommand with non-admin result")
    void testSwitchUser_NonAdmin() {
        // Session state reflect a non-admin after switching
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("subCommand", "SWITCH");
        payLoad.put("username", "user");

        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(payLoad)
                .build();

        SessionManager testSessionManager = mock(SessionManager.class);
        when(mockUserService.getSessionManager()).thenReturn(testSessionManager);
        when(testSessionManager.isAdmin()).thenReturn(false);

        String expected = ResponseStatus.USER_SWITCH_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(mockUserService, times(1)).switchUser(dummyUser);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should test EditUserHandler with Unknown subcommand")
    void testUnknownSubcommand() {
        Map<String, String> payLoad = new HashMap<>();
        payLoad.put("subCommand", "INVALID");
        payLoad.put("username", "user");

        commandDTO = CommandDTO.builder()
                .commandType("EDIT")
                .payload(payLoad)
                .build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = editUserHandler.execute(commandDTO);

        // No service method should be called for an unknown subcommand
        verify(mockUserService, never()).changePassword(any(), anyString());
        verify(mockUserService, never()).changeUserRole(any(), any());
        verify(mockUserService, never()).removeUser(any());
        verify(mockUserService, never()).switchUser(any());
        assertEquals(expected, response);
    }
}
