package com.jakub.bone.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class DataSource {
    private final String DATABASE_DIRECTORY = "src/main/resources/data";
    private final String DATABASE = "/user_db.db";
    private final String URL = String.format("jdbc:sqlite:%s", DATABASE_DIRECTORY + DATABASE);
    private static DataSource instance;
    private static Connection connection;

    public DataSource() {
        createDatabaseDirectory();
        connect();
    }

    public static synchronized DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
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


    public void connect() {
        try {
            log.debug("Attempting to connect to the database...");
            connection = DriverManager.getConnection(URL);
            if(connection != null){
                log.info("Database connection established: {}", DATABASE);
            } else {
                log.warn("Failed to establish connection to the database: {}", DATABASE);
            }
        } catch (SQLException e) {
            log.error("Database connection error: {}", e.getMessage());
            throw new RuntimeException("Error while connecting to the database", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                log.info("Database disconnected successfully");
            }
        } catch(SQLException e){
            log.error("Error while disconnecting database: {}", e.getMessage());
        }
    }
}