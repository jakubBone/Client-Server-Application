package resposne.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.Request;
import response.server.ServerDetailsResponse;
import server.ServerDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerDetailsResponseTest {
    ServerDetails mockServerDetails;
    ServerDetailsResponse serverDetailsResponse;
    Request mockRequest;

    @BeforeEach
    void setUp() {
        mockServerDetails = mock(ServerDetails.class);
        serverDetailsResponse = new ServerDetailsResponse(mockServerDetails);
        mockRequest = mock(Request.class);
    }

    @Test
    @DisplayName("Should test server uptime return")
    void testExecuteUptime() {
        when(mockRequest.getCommand()).thenReturn("UPTIME");
        when(mockServerDetails.getUptime()).thenReturn(Map.of("Days", 1L, "Hours", 2L, "Minutes", 3L, "Seconds", 4L));

        String response = serverDetailsResponse.execute(mockRequest);

        String expectedResponse = "Uptime:\n1 days, 2 hours, 3 minutes, 4 seconds";
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should test server info return")
    void testExecuteInfo() {
        when(mockRequest.getCommand()).thenReturn("INFO");
        when(mockServerDetails.getServerDetails()).thenReturn(Map.of( "Setup time", "2023-01-01 00:00:00", "Version", "1.0.0"));

        String response = serverDetailsResponse.execute(mockRequest);

        String expectedResponse = "Server Info:\nVersion = 1.0.0\nSetup time = 2023-01-01 00:00:00\n";
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should test server help return")
    void testExecuteHelp() {
        when(mockRequest.getCommand()).thenReturn("HELP");
        when(mockServerDetails.getCommands()).thenReturn(Map.of("Register", "Create a new user account", "Login", "Login to your account"));

        String response = serverDetailsResponse.execute(mockRequest);

        String expectedResponse = "Available Commands:\nRegister - Create a new user account\nLogin - Login to your account\n";
        assertEquals(expectedResponse, response);
    }
}
