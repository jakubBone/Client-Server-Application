package server;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

 /*
  * The ServerInfo class provides information about the server
  * Include server version, available commands and server uptime
  */
@Getter
@Setter
public class ServerInfo {

    private final String VERSION = "1.0.0";
    private Map<String, String> commands = new LinkedHashMap<>();
    private Map<String, String> serverDetails = new LinkedHashMap<>();
    private Map<String, Long> uptime = new LinkedHashMap<>();
    private String invalidMessage;
    private Date serverTimeCreation;

    public ServerInfo() {
        setCommands();
        setServerDetails();
        setUptime();
    }

    public void setUptime(){
        Date currentTime = new Date();
        long uptimeInMillis = currentTime.getTime() - ServerConnectionHandler.serverTimeCreation.getTime();

        long days = TimeUnit.MILLISECONDS.toDays(uptimeInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(uptimeInMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptimeInMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptimeInMillis) % 60;
        uptime.put("days", days);
        uptime.put("hours", hours);
        uptime.put("minutes", minutes);
        uptime.put("seconds", seconds);
    }

    public void setCommands() {
        commands.put("uptime", "returns the server uptime");
        commands.put("info", "returns the server version number and setup date");
        commands.put("help", "returns a list of available commands with brief descriptions");
        commands.put("stop", "stops both the server and the client");
    }

    public void setServerDetails() {
        serverDetails.put("version", VERSION);
        serverDetails.put("setup time ", String.valueOf(ServerConnectionHandler.serverTimeCreation.getTime()));
    }
}
