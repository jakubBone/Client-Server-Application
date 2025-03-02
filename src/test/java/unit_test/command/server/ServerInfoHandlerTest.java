package unit_test.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.ServerInfoHandler;
import com.jakub.bone.server.ServerInfo;
import com.jakub.bone.utils.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ServerInfoHandlerTest {
    private ServerInfo mockServerInfo;
    private ServerInfoHandler serverInfoHandler;
    private CommandDTO commandDTO;

    @BeforeEach
    void setUp() {
        mockServerInfo = mock(ServerInfo.class);
        serverInfoHandler = new ServerInfoHandler(mockServerInfo);
    }

    @Test
    @DisplayName("Should test ServerInfoHandler with UPTIME command")
    void testUptime() {
        when(mockServerInfo.getUptime()).thenReturn("Uptime: 1 day, 2 hours, 3 minutes, 4 seconds");
        commandDTO = CommandDTO.builder().commandType("UPTIME").build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(mockServerInfo, times(1)).getUptime();
        assertEquals("Uptime: 1 day, 2 hours, 3 minutes, 4 seconds", response);
    }

    @Test
    @DisplayName("Should test ServerInfoHandler with INFO command")
    void testInfo() {
        when(mockServerInfo.getInfo()).thenReturn("Version: 1.0.0\nSetup time: 2025-02-25 12:00:00");
        commandDTO = CommandDTO.builder().commandType("INFO").build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(mockServerInfo, times(1)).getInfo();
        assertEquals("Version: 1.0.0\nSetup time: 2025-02-25 12:00:00", response);
    }

    @Test
    @DisplayName("Should test ServerInfoHandler with HELP command")
    void testHelp() {
        when(mockServerInfo.getHelp()).thenReturn("Register - Create a new account\nLogin - Login to account");
        commandDTO = CommandDTO.builder().commandType("HELP").build();

        String response = serverInfoHandler.execute(commandDTO);
        verify(mockServerInfo, times(1)).getHelp();
        assertEquals("Register - Create a new account\nLogin - Login to account", response);
    }

    @Test
    @DisplayName("Should test ServerInfoHandler with Unknown command returns unknown request")
    void testUnknownCommand() {
        commandDTO = CommandDTO.builder().commandType("UNKNOWN").build();

        String expected = ResponseStatus.UNKNOWN_REQUEST.getResponse();
        String response = serverInfoHandler.execute(commandDTO);
        assertEquals(expected, response);
    }
}
