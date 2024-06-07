package operations;

import lombok.extern.log4j.Log4j2;
import server.ServerInfo;

@Log4j2
public class ServerInfoHandler {
    private final ServerInfo serverInfo = new ServerInfo();

    public String getResponse(String requestCommand) {
        log.info("Received info request command: {}", requestCommand);
        return getFormattedServerInfo(requestCommand);
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
                return builder.toString();
        }
        return builder.toString();
    }

}
