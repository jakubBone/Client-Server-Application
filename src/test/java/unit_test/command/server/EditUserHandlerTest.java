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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class EditUserHandlerTest {
    private UserService userService;
    private EditUserHandler editUserHandler;
    private CommandDTO commandDTO;
    private User dummyUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        editUserHandler = new EditUserHandler(userService);
        dummyUser = new User("user", "pass", User.Role.USER);
        when(userService.findUserByUsername("user")).thenReturn(dummyUser);
    }

    @Test
    @DisplayName("EditUserHandler - CHANGE subcommand")
    void testChangePassword() {
        commandDTO = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", "CHANGE")
                .addPayload("username", "user")
                .addPayload("newPassword", "newPass")
                .build();

        // No return value from changePassword so we expect OPERATION_SUCCEEDED response.
        String expected = ResponseStatus.OPERATION_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(userService, times(1)).changePassword(dummyUser, "newPass");
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("EditUserHandler - ASSIGN subcommand")
    void testAssignRole() {
        commandDTO = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", "ASSIGN")
                .addPayload("username", "user")
                .addPayload("newRole", "ADMIN")
                .build();

        String expected = ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(userService, times(1)).changeUserRole(dummyUser, User.Role.ADMIN);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("EditUserHandler - REMOVE subcommand")
    void testRemoveUser() {
        commandDTO = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", "REMOVE")
                .addPayload("username", "user")
                .build();

        String expected = ResponseStatus.USER_DELETE_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(userService, times(1)).removeUser(dummyUser);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("EditUserHandler - SWITCH subcommand with non-admin result")
    void testSwitchUser_NonAdmin() {
        // Let the session state reflect a non-admin after switching.
        commandDTO = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", "SWITCH")
                .addPayload("username", "user")
                .build();


        SessionManager testSessionManager = mock(SessionManager.class);
        when(userService.getSessionManager()).thenReturn(testSessionManager);
        when(testSessionManager.isAdmin()).thenReturn(false);

        String expected = ResponseStatus.USER_SWITCH_SUCCEEDED.getResponse();
        String response = editUserHandler.execute(commandDTO);

        verify(userService, times(1)).switchUser(dummyUser);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("EditUserHandler - Unknown subcommand")
    void testUnknownSubcommand() {
        commandDTO = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", "INVALID")
                .addPayload("username", "user")
                .build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = editUserHandler.execute(commandDTO);

        // No service method should be called for an unknown subcommand.
        verify(userService, never()).changePassword(any(), anyString());
        verify(userService, never()).changeUserRole(any(), any());
        verify(userService, never()).removeUser(any());
        verify(userService, never()).switchUser(any());
        assertEquals(expected, response);
    }
}
