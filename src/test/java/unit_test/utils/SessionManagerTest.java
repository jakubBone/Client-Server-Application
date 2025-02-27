package unit_test.utils;

import com.jakub.bone.domain.User;
import com.jakub.bone.session.SessionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SessionManagerTest {
    @Test
    @DisplayName("Should test current user setting in SessionManager")
    void testSetAndGetCurrentUser() {
        SessionManager sessionManager = new SessionManager();
        assertNull(sessionManager.getCurrentUser());
        User user = new User("user1", "pass123", User.Role.USER);
        sessionManager.setCurrentUser(user);
        assertEquals(user, sessionManager.getCurrentUser());
    }

    @Test
    @DisplayName("Should test authorization in SessionManager")
    void testIsAdmin() {
        SessionManager sessionManager = new SessionManager();
        // Test with non-admin user
        User user = new User("user2", "pass123", User.Role.USER);
        sessionManager.setCurrentUser(user);
        assertFalse(sessionManager.isAdmin());

        // Test with admin user
        User admin = new User("admin", "adminPass123", User.Role.ADMIN);
        sessionManager.setCurrentUser(admin);
        assertTrue(sessionManager.isAdmin());
    }
}
