package command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.ServerInfoHandler;
import com.jakub.bone.server.ServerInfo;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServerInfoHandlerTest {
    private ServerInfo serverInfo;
    private ServerInfoHandler serverInfoHandler;
    private CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        serverInfo = mock(ServerInfo.class);
        serverInfoHandler = new ServerInfoHandler(serverInfo);
    }

    @Test
    @DisplayName("ServerInfoHandler - UPTIME command")
    void testUptime() {
        when(serverInfo.getUptime()).thenReturn("Uptime: 1 day, 2 hours, 3 minutes, 4 seconds");
        commandDTO = new CommandDTO.Builder()
                .commandType("UPTIME")
                .build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(serverInfo, times(1)).getUptime();
        assertEquals("Uptime: 1 day, 2 hours, 3 minutes, 4 seconds", response);
    }

    @Test
    @DisplayName("ServerInfoHandler - INFO command")
    void testInfo() {
        when(serverInfo.getInfo()).thenReturn("Version: 1.0.0\nSetup time: 2025-02-25 12:00:00");
        commandDTO = new CommandDTO.Builder()
                .commandType("INFO")
                .build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(serverInfo, times(1)).getInfo();
        assertEquals("Version: 1.0.0\nSetup time: 2025-02-25 12:00:00", response);
    }

    @Test
    @DisplayName("ServerInfoHandler - HELP command")
    void testHelp() {
        when(serverInfo.getHelp()).thenReturn("Register - Create a new account\nLogin - Login to account");
        commandDTO = new CommandDTO.Builder()
                .commandType("HELP")
                .build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(serverInfo, times(1)).getHelp();
        assertEquals("Register - Create a new account\nLogin - Login to account", response);
    }

    @Test
    @DisplayName("ServerInfoHandler - Unknown command returns unknown request")
    void testUnknownCommand() {
        commandDTO = new CommandDTO.Builder()
                .commandType("UNKNOWN")
                .build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = serverInfoHandler.execute(commandDTO);
        assertEquals(expected, response);
    }
}
