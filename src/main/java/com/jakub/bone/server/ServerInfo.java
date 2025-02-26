package com.jakub.bone.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.jakub.bone.network.ServerConnectionManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class ServerInfo {
    private final String version = "1.0.0";
    private final Map<String, String> commands = new LinkedHashMap<>();
    private final Map<String, String> serverDetails = new LinkedHashMap<>();
    private final Map<String, Long> uptime = new LinkedHashMap<>();

    public ServerInfo() {
        setCommands();
        setServerDetails();
    }

    public void setCommands() {
        commands.put("Register", "Create a new user account");
        commands.put("Login", "Log in to account");
        commands.put("Help", "All commands list");
        commands.put("Uptime", "Check server uptime");
        commands.put("Info", "Server details");
        commands.put("Exit", "Stop application");

    }

    public void setServerDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String setupTimeFormatted = dateFormat.format(ServerConnectionManager.startTime);
        serverDetails.put("Version", version);
        serverDetails.put("Setup time", setupTimeFormatted);
    }

    public String getUptime() {
        long diff = new Date().getTime() - ServerConnectionManager.startTime.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
        return String.format("Uptime: %d days, %d hours, %d minutes, %d seconds",
                days, hours, minutes, seconds);
    }

    public String getInfo() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : serverDetails.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        // Remove last sign if '\n'
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n') {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n') {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
