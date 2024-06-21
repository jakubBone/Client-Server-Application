package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DataBase {
    private final String USERNAME = "client_manager";
    private final String PASSWORD = "client123";
    private final String DATABASE = "user_db";
    private final int PORT_NUMBER = 5432;
    private final String URL = String.format("jdbc:postgresql://localhost:%d/%s", PORT_NUMBER, DATABASE);
    private Connection connection;

    public DataBase() {
        connection = getNewConnection();
        log.info("Connection with {} database established on port {}", USERNAME, PORT_NUMBER);
    }

    private Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                log.info("Database disconnected");
            }
        } catch(SQLException ex){
            log.error("Error during database disconnection: {}", ex.getMessage());
        }
    }
}
