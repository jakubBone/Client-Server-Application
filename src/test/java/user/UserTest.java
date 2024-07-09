package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import user.credential.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;
    @BeforeEach
    void setUp() {
        user = new User("testUser", "testPassword", User.Role.USER);
    }

    @Test
    @DisplayName("Should test password hashing")
    void testHashPassword() {
        user.hashPassword();

        assertTrue(BCrypt.checkpw("testPassword", user.getHashedPassword()));
    }
    @Test
    @DisplayName("Should test password setting")
    void testSetPassword() {
        user.setPassword("testPassword");

        assertNotNull(user.getPassword());
        assertEquals("testPassword", user.getPassword());
    }
}