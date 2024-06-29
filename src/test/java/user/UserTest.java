package user;

import database.DatabaseConnection;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    DatabaseConnection DATABASE;
    DSLContext JOOQ;

    @BeforeEach
    void setUp() {
        DATABASE = new DatabaseConnection();
        JOOQ = DSL.using(DATABASE.getConnection());
    }

    @Test
    @DisplayName("Should test password matching functionality with correct password")
    void testCheckPasswordCorrect() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "examplePassword";

        boolean isMatching =  user.checkPassword(typedPassword, JOOQ );

        assertTrue(isMatching);
    }

    @Test
    @DisplayName("Should test password matching functionality with incorrect password")
    void testCheckPasswordIncorrect() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "incorrectPassword";

        boolean isMatching =  user.checkPassword(typedPassword, JOOQ);

        assertFalse(isMatching);
    }

    @Test
    @DisplayName("Should test password hashing functionality")
    void testHashPassword() {
        User user = new User("exampleUser", "examplePassword", User.Role.USER);
        String typedPassword = "examplePassword";

        user.hashPassword();

        assertNotNull(user.getHashedPassword());
        assertTrue(user.checkPassword(typedPassword, JOOQ));
    }
}