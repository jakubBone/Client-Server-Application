package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DatabaseConnection {
    private final String DATABASE_DIRECTORY = "src/main/resources/data";
    private final String DATABASE = DATABASE_DIRECTORY + "/user_db.sqlite";
    private final String URL = String.format("jdbc:sqlite:%s", DATABASE);
    private static DatabaseConnection instance;
    private static Connection connection;

    public DatabaseConnection() {
        createDatabaseDirectory();
        connect();
    }

    private void createDatabaseDirectory() {
        File directory = new File(DATABASE_DIRECTORY);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Created directory for database at {}", DATABASE_DIRECTORY);
            } else {
                log.info("Failed to create directory for database at {}", DATABASE_DIRECTORY);
            }
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connect() {
        try {
            log.info("Attempting to connect with data base");
            connection = DriverManager.getConnection(URL);
            if(connection != null){
                log.info("Connection with database {} established", DATABASE);
            } else {
                log.info("Failed to connect with database {}", DATABASE);
            }
        } catch (SQLException ex) {
            log.error("Error during database connection: {}", ex.getMessage());

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