package unit_test.repository;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.domain.User;
import com.jakub.bone.repository.UserRepository;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    UserRepository userRepository;
    Connection connection;

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
    @DisplayName("Should test create and find user")
    void testCreateAndFindUser() {
        User user = new User("user1", "pass123", User.Role.USER);
        userRepository.createUser(user);

        User found = userRepository.findUserByUsername("user1");
        assertNotNull(found);
        assertEquals("user1", found.getUsername());
    }

    @Test
    @DisplayName("Should test verify user password")
    void testVerifyUserPassword() {
        User user = new User("user2", "pass123", User.Role.USER);
        userRepository.createUser(user);

        boolean correct = userRepository.verifyUserPassword("pass123", user.getUsername());
        assertTrue(correct, "Password should be verified correctly");

        boolean wrong = userRepository.verifyUserPassword("wrong", "user2");
        assertFalse(wrong);
    }

    @Test
    @DisplayName("Should test update user")
    void testUpdateUser() {
        User user = new User("user3", "oldPass", User.Role.USER);
        userRepository.createUser(user);

        // After password change the setter hashes the new password
        user.setPassword("newPass");
        userRepository.updateUser(user);

        User updated = userRepository.findUserByUsername("user3");
        assertNotNull(updated);
        assertTrue(userRepository.verifyUserPassword("newPass", "user3"));
    }

    @Test
    @DisplayName("Should test remove user")
    void testRemoveUser() {
        User user = new User("user4", "pass123", User.Role.USER);
        userRepository.createUser(user);

        userRepository.removeUser("user4");
        User found = userRepository.findUserByUsername("user4");
        assertNull(found);
    }
}
