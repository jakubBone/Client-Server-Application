package utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonConverter class.
 * This class tests the conversion of objects to and from JSON format, ensuring correct serialization and deserialization.
 */
public class JsonConverterTest {

    @Test
    @DisplayName("Should test message serializing to JSon")
    void testSerializeMessage() {
        String message = "exampleMessage";
        JsonConverter converter = new JsonConverter(message);
        String expectedFormat = "{\"message\":\"exampleMessage\"}\n<<END>>";

        // Test JSON serialization
        String serializedMessage = converter.serializeMessage();

        assertNotNull(serializedMessage);
        assertEquals(serializedMessage, expectedFormat);
    }

    @Test
    @DisplayName("Should test message deserializing from JSon")
    void testDeserializeMessage() {
        String message = "exampleMessage";
        JsonConverter converter = new JsonConverter(message);

        String serializedMessage = converter.serializeMessage();

        // Removing "\n<<END>>" from the end of JSon message
        String jsonWithoutEndTag = serializedMessage.replace("\n<<END>>", "");

        // Test JSON deserialization
        String deserializedMessage = converter.deserializeMessage(jsonWithoutEndTag);

        assertNotNull(deserializedMessage);
        assertEquals(message, deserializedMessage);
    }
}
