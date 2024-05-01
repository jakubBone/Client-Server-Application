package operations;

import lombok.extern.log4j.Log4j2;
import server.ServerInfo;

@Log4j2
public class HelpHandler {
    private final ServerInfo serverInfo = new ServerInfo();

    public String getHelpRequest(String request) {
        log.info("Received help request: {}", request);
        String response = null;
        switch (request) {
            case "UPTIME":
                response = serverInfo.getUptime().toString();
                log.info("UPTIME requested, response: {}", response);
                break;
            case "INFO":
                response = serverInfo.getServerDetails().toString();
                log.info("INFO requested, response: {}", response);
                break;
            case "HELP":
                response = serverInfo.getCommands().toString();
                log.info("HELP requested, response: {}", response);
                break;
        }
        return response;
    }
}
