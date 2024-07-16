package database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseConnectionTest {
    static DatabaseConnection databaseConnection;
    static Connection mockConnection;


    @BeforeEach
    void setUp() {
        databaseConnection  = DatabaseConnection.getInstance();
        mockConnection = mock(Connection.class);
    }

    @AfterAll
    static void closeDown() {
        databaseConnection.disconnect();
    }

    @Test
    @DisplayName("Should test connection with data base")
    void testStartConnection()  {
        assertNotNull(databaseConnection.getConnection());
    }

    @Test
    @DisplayName("Should test data base connection return")
    void testGetConnection()  {
        Connection connection = databaseConnection.getConnection();

        assertNotNull(connection);
    }

    @Test
    @DisplayName("Should test data base disconnect")
    void testDisconnect() throws SQLException {
        Connection connection = databaseConnection.getConnection();

        databaseConnection.disconnect();

        assertTrue(connection.isClosed());
    }
}
