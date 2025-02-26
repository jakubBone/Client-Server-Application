package unit_test.utils;

import com.jakub.bone.domain.User;
import com.jakub.bone.utils.JsonConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonConverterTest {
    @Test
    void testSerializeAndDeserialize() {
        User originalUser = new User("john", "secret", User.Role.USER);

        // Serialize the User object to JSON.
        String json = JsonConverter.serialize(originalUser);
        assertNotNull(json, "Serialized JSON should not be null");
        System.out.println("Serialized JSON: " + json);

        // Deserialize the JSON back into a User object.
        User deserializedUser = JsonConverter.deserialize(json, User.class);
        assertNotNull(deserializedUser, "Deserialized User should not be null");

        // Verify that the username and role are preserved.
        assertEquals(originalUser.getUsername(), deserializedUser.getUsername(),
                "Username should be preserved after deserialization");
        assertEquals(originalUser.getRole(), deserializedUser.getRole(),
                "User role should be preserved after deserialization");

        // Although the password is hashed internally, we can check that they are not null.
        assertNotNull(deserializedUser.getPassword(), "Deserialized password should not be null");
        assertNotNull(deserializedUser.getHashedPassword(), "Deserialized hashedPassword should not be null");
    }
}
