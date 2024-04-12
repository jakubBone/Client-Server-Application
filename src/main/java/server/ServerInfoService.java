package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.PrintWriter;
import java.util.Date;

public class ServerInfoService {
    private static final Logger logger = LogManager.getLogger(ServerInfoService.class);
    private final Gson gson;
    private final ServerInfo serverInfo;

    public ServerInfoService(String version, Date serverTimeCreation) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.serverInfo = new ServerInfo(version, serverTimeCreation);
    }

    public void handleHelpRequest(String request, PrintWriter outToClient) {
        String json;
        switch (request) {
            case "UPTIME":
                json = gson.toJson(serverInfo.getUptime());
                logger.info("Time from server setup: " + serverInfo.getUptime());
                break;
            case "INFO":
                json = gson.toJson(serverInfo.getServerDetails());
                logger.info("Server version: " + serverInfo.getVersion() + " / Setup date: " + serverInfo.getServerTimeCreation());
                break;
            case "HELP":
                json = gson.toJson(serverInfo.getCommands());
                logger.info("Command list displayed");
                break;
            default:
                json = gson.toJson(serverInfo.getInvalidMessage());
                logger.warn("Invalid input ---------");
        }
        json += "\n<<END>>\n";
        outToClient.println(json);
    }
}