package com.jakub.bone.utils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                log.error("Configuration file 'config.properties' not found in classpath");
                throw new RuntimeException("Configuration file 'config.properties' not found in classpath");
            }
            properties.load(input);
            log.info("Configuration file loaded successfully");
        } catch (IOException ex) {
            log.error("Failed to load configuration file", ex);
            throw new RuntimeException("Failed to load configuration file", ex);
        }
    }

    public static String get(String key) {
        // For the key check if the environment variable exists
        // e.g 'database.url' -> 'DATABASE_URL'
        String envKey = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return properties.getProperty(key);
    }
}
