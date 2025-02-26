package integration_test;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.network.ServerConnectionManager;
import com.jakub.bone.server.RequestProcessor;
import com.jakub.bone.utils.JsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Date;

import static com.jakub.bone.utils.ResponseStatus.LOGOUT_SUCCEEDED;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleRequestProcessorTest {
    @Test
    @DisplayName("Test RequestProcessor handling LOGOUT command using StringWriter")
    void testLogoutCommandProcessing() throws IOException {
        ServerConnectionManager.startTime = new Date();

        CommandDTO logoutCommand = new CommandDTO.Builder()
                .commandType("LOGOUT")
                .build();
        String jsonCommand = JsonConverter.serialize(logoutCommand) + "\n<<END>>\n";

        // Simulate receiving by using the sent data as input
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonCommand.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(byteArrayInputStream));

        // Use StringWriter to capture output
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter, true);

        RequestProcessor processor = new RequestProcessor(out, in);
        processor.start();

        String output = stringWriter.toString();
        String expectedResponse = LOGOUT_SUCCEEDED.getResponse();

        assertTrue(output.contains(expectedResponse));
    }
}
