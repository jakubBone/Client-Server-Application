package repository;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.User;
import com.jakub.bone.repository.UserRepository;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {
    private UserRepository userRepository;
    private Connection connection;

    @BeforeAll
    void setUp() {
        connection = DataSource.getInstance().getConnection();
        userRepository = new UserRepository();
    }

    @AfterEach
    void cleanUp() {
        userRepository.truncateTable();
    }

    @Test
    @DisplayName("Create and find user")
    void testCreateAndFindUser() {
        User user = new User("testuser", "password", User.Role.USER);
        userRepository.createUser(user);

        User found = userRepository.findUserByUsername("testuser");
        assertNotNull(found, "User should be found");
        assertEquals("testuser", found.getUsername(), "Username should be 'testuser'");
    }

    @Test
    @DisplayName("Verify user password")
    void testVerifyUserPassword() {
        User user = new User("verifyUser", "secret", User.Role.USER);
        userRepository.createUser(user);

        boolean correct = userRepository.verifyUserPassword("secret", user.getUsername());
        assertTrue(correct, "Password should be verified correctly");

        boolean wrong = userRepository.verifyUserPassword("wrong", "verifyUser");
        assertFalse(wrong, "Incorrect password should be rejected");
    }

    @Test
    @DisplayName("Update user")
    void testUpdateUser() {
        User user = new User("updateUser", "oldPass", User.Role.USER);
        userRepository.createUser(user);

        // Changing the password – the setter should hash the new password
        user.setPassword("newPass");
        userRepository.updateUser(user);

        User updated = userRepository.findUserByUsername("updateUser");
        assertNotNull(updated, "Updated user should exist");
        assertTrue(userRepository.verifyUserPassword("newPass", "updateUser"), "New password should be verified correctly");
    }

    @Test
    @DisplayName("Remove user")
    void testRemoveUser() {
        User user = new User("removeUser", "pass", User.Role.USER);
        userRepository.createUser(user);

        userRepository.removeUser("removeUser");
        User found = userRepository.findUserByUsername("removeUser");
        assertNull(found, "User should be removed from the database");
    }
}
