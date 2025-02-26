package command.client;

import com.jakub.bone.command.client.*;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class CommandCreationTests {

    private UserInput mockInput;

    @BeforeEach
    void setUp() {
        mockInput = mock(UserInput.class);
    }

    @Test
    @DisplayName("DeleteMailCommand builds correct CommandDTO")
    void testDeleteMailCommand() throws IOException {
        // simulate user input for mailbox type
        when(mockInput.getRequest()).thenReturn("inbox");

        DeleteMailCommand command = new DeleteMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("DELETE", dto.getCommandType());
        assertEquals("INBOX", dto.getPayload().get("boxType"));
    }

    @Test
    @DisplayName("NewMailCommand builds correct CommandDTO")
    void testNewMailCommand() throws IOException {
        when(mockInput.promptRecipient()).thenReturn("recipientUser");
        when(mockInput.promptMessage()).thenReturn("Hello World");

        NewMailCommand command = new NewMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("NEW", dto.getCommandType());
        assertEquals("recipientUser", dto.getPayload().get("recipient"));
        assertEquals("Hello World", dto.getPayload().get("message"));
    }

    @Test
    @DisplayName("ReadMailCommand builds correct CommandDTO with valid box type")
    void testReadMailCommand() throws IOException {
        // First call returns a valid box type, so no need to loop
        when(mockInput.getRequest()).thenReturn("SENT");

        ReadMailCommand command = new ReadMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("READ", dto.getCommandType());
        assertEquals("SENT", dto.getPayload().get("boxType"));
    }

    @Test
    @DisplayName("LogoutCommand builds correct CommandDTO")
    void testLogoutCommand() throws IOException {
        LogoutCommand command = new LogoutCommand();
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("LOGOUT", dto.getCommandType());
        assertTrue(dto.getPayload().isEmpty());
    }

    @Test
    @DisplayName("ServerInfoCommand builds correct CommandDTO for HELP command")
    void testServerInfoCommand_Help() {
        ServerInfoCommand command = new ServerInfoCommand("help");
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("HELP", dto.getCommandType());
        assertTrue(dto.getPayload().isEmpty());
    }

    @Test
    @DisplayName("EditUserCommand builds correct CommandDTO for CHANGE subcommand")
    void testEditUserCommandChange() throws IOException {
        // For EditUserCommand, simulate the following inputs:
        // First, user enters "CHANGE" as subcommand.
        // Then, promptUsername() returns a username,
        // and promptNewPassword() returns a new password.
        when(mockInput.getRequest()).thenReturn("CHANGE");
        when(mockInput.promptUsername()).thenReturn("user1");
        when(mockInput.promptNewPassword()).thenReturn("newPass");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("CHANGE", dto.getPayload().get("subCommand"));
        assertEquals("user1", dto.getPayload().get("username"));
        assertEquals("newPass", dto.getPayload().get("newPassword"));
    }

    @Test
    @DisplayName("EditUserCommand builds correct CommandDTO for ASSIGN subcommand")
    void testEditUserCommand_ssign() throws IOException {
        // For ASSIGN, simulate:
        // subCommand "ASSIGN", then promptUsername() and promptNewRole() returns valid role.
        when(mockInput.getRequest()).thenReturn("ASSIGN");
        when(mockInput.promptUsername()).thenReturn("user2");
        // First call to promptNewRole returns an invalid role to force re-prompt,
        // then a valid one ("ADMIN")
        when(mockInput.promptNewRole())
                .thenReturn("invalidRole", "ADMIN");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("ASSIGN", dto.getPayload().get("subCommand"));
        assertEquals("user2", dto.getPayload().get("username"));
        assertEquals("ADMIN", dto.getPayload().get("newRole"));
    }

    @Test
    @DisplayName("EditUserCommand builds correct CommandDTO for REMOVE subcommand")
    void testEditUserCommandRemove() throws IOException {
        // For REMOVE, simulate:
        // subCommand "REMOVE", then promptUsername() returns a username.
        when(mockInput.getRequest()).thenReturn("REMOVE");

        when(mockInput.promptUsername()).thenReturn("user3");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("REMOVE", dto.getPayload().get("subCommand"));
        assertEquals("user3", dto.getPayload().get("username"));
    }

    @Test
    @DisplayName("EditUserCommand builds correct CommandDTO for SWITCH subcommand")
    void testEditUserCommandSwitch() throws IOException {
        // For SWITCH, simulate:
        // subCommand "SWITCH", then promptUsername() returns a username.
        when(mockInput.getRequest()).thenReturn("SWITCH");
        when(mockInput.promptUsername()).thenReturn("user4");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("SWITCH", dto.getPayload().get("subCommand"));
        assertEquals("user4", dto.getPayload().get("username"));
    }

    // --- Note on AuthCommand ---
    // AuthCommand currently instantiates a new UserInput within buildCommandMessage().
    // For proper testing, it is recommended to refactor AuthCommand to use the injected instance.
    // For demonstration, one approach is to create a TestUserInput subclass that overrides promptUsername
    // and promptPassword. Then you could override the instantiation in the test (for example, using a factory
    // or dependency injection). Here we simply note that AuthCommand should be refactored to allow testability.
}

