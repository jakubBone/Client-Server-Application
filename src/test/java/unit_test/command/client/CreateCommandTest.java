package unit_test.command.client;

import com.jakub.bone.command.client.*;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.ConsolerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateCommandTest {
    ConsolerReader mockInput;

    @BeforeEach
    void setUp() {
        mockInput = mock(ConsolerReader.class);
    }

    @Test
    @DisplayName("Should test DeleteMailCommand builds correct CommandDTO")
    void testDeleteMailCommand() throws IOException {
        // Simulate user input for mailbox type
        when(mockInput.getRequest()).thenReturn("DELETE");
        when(mockInput.promptMailbox()).thenReturn("INBOX");

        DeleteMailCommand command = new DeleteMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("DELETE", dto.getCommandType());
        assertEquals("INBOX", dto.getPayload().get("boxType"));
    }

    @Test
    @DisplayName("Should test NewMailCommand builds correct CommandDTO")
    void testNewMailCommand() throws IOException {
        when(mockInput.promptRecipient()).thenReturn("recipient");
        when(mockInput.promptMessage()).thenReturn("Hello");

        NewMailCommand command = new NewMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("NEW", dto.getCommandType());
        assertEquals("recipient", dto.getPayload().get("recipient"));
        assertEquals("Hello", dto.getPayload().get("message"));
    }

    @Test
    @DisplayName("Should test ReadMailCommand builds correct CommandDTO with valid box type")
    void testReadMailCommand() throws IOException {
        when(mockInput.getRequest()).thenReturn("READ");
        when(mockInput.promptMailbox()).thenReturn("SENT");

        ReadMailCommand command = new ReadMailCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("READ", dto.getCommandType());
        assertEquals("SENT", dto.getPayload().get("boxType"));
    }

    @Test
    @DisplayName("Should test LogoutCommand builds correct CommandDTO")
    void testLogoutCommand() throws IOException {
        LogoutCommand command = new LogoutCommand("LOGOUT");
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("LOGOUT", dto.getCommandType());
        assertTrue(dto.getPayload().isEmpty());
    }

    @Test
    @DisplayName("Should test ServerInfoCommand builds correct CommandDTO for HELP command")
    void testServerInfoCommand_Help() {
        ServerInfoCommand command = new ServerInfoCommand("HELP");
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("HELP", dto.getCommandType());
        assertTrue(dto.getPayload().isEmpty());
    }

    @Test
    @DisplayName("Should test EditUserCommand builds correct CommandDTO for CHANGE subcommand")
    void testEditUserCommandChange() throws IOException {
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
    @DisplayName("Should test EditUserCommand builds correct CommandDTO for ASSIGN subcommand")
    void testEditUserCommandAssign() throws IOException {
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
    @DisplayName("Should test EditUserCommand builds correct CommandDTO for REMOVE subcommand")
    void testEditUserCommandRemove() throws IOException {
        when(mockInput.getRequest()).thenReturn("REMOVE");
        when(mockInput.promptUsername()).thenReturn("user3");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("REMOVE", dto.getPayload().get("subCommand"));
        assertEquals("user3", dto.getPayload().get("username"));
    }

    @Test
    @DisplayName("Should test EditUserCommand builds correct CommandDTO for SWITCH subcommand")
    void testEditUserCommandSwitch() throws IOException {
        when(mockInput.getRequest()).thenReturn("SWITCH");
        when(mockInput.promptUsername()).thenReturn("user4");

        EditUserCommand command = new EditUserCommand(mockInput);
        CommandDTO dto = command.buildCommandMessage();

        assertEquals("EDIT", dto.getCommandType());
        assertEquals("SWITCH", dto.getPayload().get("subCommand"));
        assertEquals("user4", dto.getPayload().get("username"));
    }
}
