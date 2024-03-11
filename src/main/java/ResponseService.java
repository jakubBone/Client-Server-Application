import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseService {
    private String uptime;
    private String version;
    private Date creationDate;
    private Map<String, String> commands = new HashMap<>();
    private String message;


    public void setMessage(String message) {
        this.message = message;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCommands() {
        commands.put("help", "returns a list of available commands with brief descriptions");
        commands.put("info", "returns the server's version number and creation date");
        commands.put("uptime", "returns the server's uptime");
        commands.put("stop", "stops both the server and the client");
    }
}
