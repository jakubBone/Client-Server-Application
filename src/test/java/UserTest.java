
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import user.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for User class.
 * This class tests user-related functionalities such as password matching, hashing, and role management.
 */
public class UserTest {

    @Test
    @DisplayName("Should test password matching functionality with correct password")
    void testCheckPasswordCorrect() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "examplePassword";

        boolean isMatching =  user.checkPassword(typedPassword);

        assertTrue(isMatching);
    }

    @Test
    @DisplayName("Should test password matching functionality with incorrect password")
    void testCheckPasswordIncorrect() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "incorrectPassword";

        boolean isMatching =  user.checkPassword(typedPassword);

        assertFalse(isMatching);
    }

    @Test
    @DisplayName("Should test password hashing functionality")
    void testHashPassword() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "examplePassword";

        user.hashPassword();

        assertNotNull(user.getHashedPassword());
        assertTrue(user.checkPassword(typedPassword));
    }
}
