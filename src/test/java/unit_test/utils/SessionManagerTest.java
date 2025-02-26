package unit_test.utils;

import com.jakub.bone.domain.User;
import com.jakub.bone.session.SessionManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SessionManagerTest {
    @Test
    void testSetAndGetCurrentUser() {
        SessionManager sessionManager = new SessionManager();
        assertNull(sessionManager.getCurrentUser(), "Initially, current user should be null");
        User user = new User("john", "password", User.Role.USER);
        sessionManager.setCurrentUser(user);
        assertEquals(user, sessionManager.getCurrentUser(), "Current user should be set correctly");
    }

    @Test
    void testIsAdmin() {
        SessionManager sessionManager = new SessionManager();
        // Test with non-admin user
        User user = new User("john", "password", User.Role.USER);
        sessionManager.setCurrentUser(user);
        assertFalse(sessionManager.isAdmin(), "Non-admin user should return false for isAdmin");

        // Test with admin user
        User admin = new User("admin", "adminpass", User.Role.ADMIN);
        sessionManager.setCurrentUser(admin);
        assertTrue(sessionManager.isAdmin(), "Admin user should return true for isAdmin");
    }
}
