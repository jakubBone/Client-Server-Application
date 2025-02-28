package com.jakub.bone.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jakub.bone.utils.ConfigLoader;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class DataSource {
    private final String USER = ConfigLoader.get("database.username");
    private final String PASSWORD = ConfigLoader.get("database.password");
    private final String DATABASE = ConfigLoader.get("database.name");
    private final String PORT_NUMBER = ConfigLoader.get("database.port");
    private final String URL = String.format("jdbc:postgresql://db:%s/%s", PORT_NUMBER, DATABASE);
    private static DataSource instance;
    private static Connection connection;

    public DataSource() {
        connect();
    }

    public static synchronized DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("Attempting to connect with data base");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                log.info("Connection with {} database established on port {}", USER, PORT_NUMBER);
            } else {
                log.info("Failed to connect with {} database established on port {}", USER, PORT_NUMBER);
            }
        } catch (SQLException e) {
            log.error("Error during database connection: {}", e.getMessage());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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