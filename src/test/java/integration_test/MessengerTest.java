package integration_test;

import com.jakub.bone.utils.Messenger;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessengerTest {
    @Test
    void testSendAndReceive() {
        String testMessage = "Hello!";

        // Create an output stream to capture the sent message
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(baos, true);

        // Create a Messenger instance that writes to baos.
        // For this test, the input stream is not used during sending.
        Messenger messenger = new Messenger(out, new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0]))));

        // Send the dummy message.
        messenger.send(testMessage);

        // Retrieve the sent data from the output stream.
        String sentJson = baos.toString();
        // The protocol appends "\n<<END>>", so the sentJson should contain that.

        // Now simulate receiving by using the sent data as input.
        ByteArrayInputStream bais = new ByteArrayInputStream(sentJson.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(bais));

        // Create a new Messenger instance for receiving.
        Messenger messengerForReceive = new Messenger(new PrintWriter(System.out, true), in);
        String received = messengerForReceive.receive(String.class);

        // The Messenger uses JsonConverter internally, so the received object should equal the original.
        assertEquals(testMessage, received, "The received message should equal the original dummy message");
    }
}
