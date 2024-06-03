package operations;

import lombok.extern.log4j.Log4j2;
import server.ServerInfo;

@Log4j2
public class ServerInfoHandler {
    private final ServerInfo serverInfo = new ServerInfo();

    public String getServerInfoRequest(String request) {
        log.info("Received info request: {}", request);
        return getFormattedServerInfo(request);
    }

    private String getFormattedServerInfo(String request) {
        StringBuilder builder = new StringBuilder();
        switch (request.toUpperCase()) {
            case "UPTIME":
                builder.append("Uptime:\n");
                builder.append(String.format("%d days, %d hours, %d minutes, %d seconds",
                        serverInfo.getUptime().get("Days"),
                        serverInfo.getUptime().get("Hours"),
                        serverInfo.getUptime().get("Minutes"),
                        serverInfo.getUptime().get("Seconds")));
                break;
            case "INFO":
                builder.append("Server Info:\n");
                serverInfo.getServerDetails().forEach((key, value) -> builder.append(key).append(" = ").append(value).append("\n"));
                break;
            case "HELP":
                builder.append("Available Commands:\n");
                serverInfo.getCommands().forEach((key, value) -> builder.append(key).append(" - ").append(value).append("\n"));
                return builder.toString(); // For HELP, return immediately to avoid removing the last comma
        }
        return builder.toString();
    }

}
