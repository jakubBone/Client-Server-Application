package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DataBaseConnection {
    private final String USER = "user_manager";
    private final String PASSWORD = "user123";
    private final String DATABASE = "user_db";
    private final int PORT_NUMBER = 5432;
    private final String URL = String.format("jdbc:postgresql://localhost:%d/%s", PORT_NUMBER, DATABASE);
    private static Connection connection;

    public DataBaseConnection() {
        startConnection();
    }

    private void startConnection() {
        try {
            log.info("Attempting to connect with data base");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if(connection != null){
                log.info("Connection with {} database established on port {}", USER, PORT_NUMBER);
            } else {
                log.info("Failed to connect with {} database established on port {}", USER, PORT_NUMBER);
            }
        } catch (SQLException ex) {
            log.error("Error during database connection: {}", ex.getMessage());
            throw new RuntimeException("Error establishing database connection", ex);
        }
    }

    public Connection getConnection(){
        return connection;
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