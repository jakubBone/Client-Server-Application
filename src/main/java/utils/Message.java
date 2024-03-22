package utils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Message {
    private Map<String, String> commands = new LinkedHashMap<>();
    private Map<String, String> serverDetails = new LinkedHashMap<>();
    private Map<String, Long> uptime = new LinkedHashMap<>();
    private String invalidMessage;

    public Message(String version, Date serverTimeCreation) {
        setCommands();
        setServerDetails(version, serverTimeCreation);
        setUptime(serverTimeCreation);
        setInvalidMessage();
    }

    public Map<String, Long> getUptime() {
        return uptime;
    }

    public void setUptime(Date serverTimeCreation){
        Date currentTime = new Date();
        long uptimeInMillis = currentTime.getTime() - serverTimeCreation.getTime();

        long days = TimeUnit.MILLISECONDS.toDays(uptimeInMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(uptimeInMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptimeInMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptimeInMillis) % 60;
        uptime.put("days", days);
        uptime.put("hours", hours);
        uptime.put("minutes", minutes);
        uptime.put("seconds", seconds);
    }

    public Map<String, String> getCommands() {
        return commands;
    }

    public void setCommands() {
        commands.put("uptime", "returns the server uptime");
        commands.put("info", "returns the server version number and setup date");
        commands.put("help", "returns a list of available commands with brief descriptions");
        commands.put("stop", "stops both the server and the client");
    }

    public Map<String, String> getServerDetails() {
        return serverDetails;
    }

    public void setServerDetails(String version, Date serverTimeCreation) {
        serverDetails.put("version", version);
        serverDetails.put("setup time ", serverTimeCreation.toString());
    }

    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage() {
       invalidMessage = "Invalid input";
    }
}
