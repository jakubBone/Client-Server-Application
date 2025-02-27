package unit_test.utils;

import com.jakub.bone.domain.User;
import com.jakub.bone.utils.JsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonConverterTest {
    @Test
    @DisplayName("Should test user serializing and deserializing")
    void testSerializeAndDeserialize() {
        User originalUser = new User("user1", "pass123", User.Role.USER);

        // Serialize the User object to JSON
        String json = JsonConverter.serialize(originalUser);
        assertNotNull(json);

        // Deserialize the JSON back into a User object
        User deserializedUser = JsonConverter.deserialize(json, User.class);
        assertNotNull(deserializedUser);

        // Verify that the username and role are preserved
        assertEquals(originalUser.getUsername(), deserializedUser.getUsername());
        assertEquals(originalUser.getRole(), deserializedUser.getRole());

        // The password is hashed internally
        assertNotNull(deserializedUser.getPassword());
        assertNotNull(deserializedUser.getHashedPassword());
    }
}
