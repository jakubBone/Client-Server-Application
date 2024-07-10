package server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * The ServerDetails class provides information about the server.
 */
@Getter
@Setter
@Log4j2
public class ServerDetails {
    private final String VERSION = "1.0.0";
    private Map<String, String> commands = new LinkedHashMap<>();
    private Map<String, String> serverDetails = new LinkedHashMap<>();
    private Map<String, Long> uptime = new LinkedHashMap<>();

    public ServerDetails() {
        setCommands();
        setServerDetails();
        setUptime();
    }

   public void setUptime(){
       Date currentTime = new Date();
       long uptimeInMillis = currentTime.getTime() - ServerConnection.serverTimeCreation.getTime();

       long days = TimeUnit.MILLISECONDS.toDays(uptimeInMillis);
       long hours = TimeUnit.MILLISECONDS.toHours(uptimeInMillis) % 24;
       long minutes = TimeUnit.MILLISECONDS.toMinutes(uptimeInMillis) % 60;
       long seconds = TimeUnit.MILLISECONDS.toSeconds(uptimeInMillis) % 60;

       uptime.put("Days", days);
       uptime.put("Hours", hours);
       uptime.put("Minutes", minutes);
       uptime.put("Seconds", seconds);
    }

    public void setCommands() {
        commands.put("Login", "Login to your account");
        commands.put("Register", "Create a new user account");
        commands.put("Help", "All commands list");
        commands.put("Uptime", "Check server uptime");
        commands.put("Info", "Server version and date");
        commands.put("Exit", "Stop server and client");

    }

    public void setServerDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String setupTimeFormatted = dateFormat.format(ServerConnection.serverTimeCreation);
        serverDetails.put("Version", VERSION);
        serverDetails.put("Setup time", setupTimeFormatted);
    }
}
