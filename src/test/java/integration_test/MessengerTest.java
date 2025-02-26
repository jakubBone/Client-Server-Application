package integration_test;

import com.jakub.bone.utils.Messenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessengerTest {
    @Test
    @DisplayName("Should test communication between client and server")
    void testSendAndReceive() {
        String testMessage = "Hello!";

        // Create an output stream to capture the message
        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(byteArrayOutputStream, true);

        // Create a Messenger that writes to byteArrayOutputStream
        // For this test input stream is not used during sending
        Messenger messenger = new Messenger(out, new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0]))));

        messenger.send(testMessage);

        // Retrieve the sent data from the output stream
        String sentJson = byteArrayOutputStream.toString();

        // Simulate receiving by using the sent data as input
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sentJson.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(byteArrayInputStream));

        // Create a new Messenger or receiving
        Messenger messengerForReceive = new Messenger(new PrintWriter(System.out, true), in);
        String received = messengerForReceive.receive(String.class);

        // Messenger uses JsonConverter internally
        assertEquals(testMessage, received);
    }
}
