package handler.server;

import lombok.extern.log4j.Log4j2;
import server.ServerDetails;
import shared.ResponseMessage;

/**
 * The ServerDetailsHandler class provides responses for server information requests.
 * It can return server uptime, version, and available commands.
 */

@Log4j2
public class ServerDetailsHandler {
    private final ServerDetails serverInfo = new ServerDetails();

    /**
     * Processes server information requests and generates appropriate responses.
     * @param requestCommand The command for the request (e.g., "UPTIME", "INFO", "HELP")
     * @return The response message as a string
     */
    public String getResponse(String requestCommand) {
        log.info("Received info request command: {}", requestCommand);
        return getFormattedServerInfo(requestCommand);
    }

    /**
     * Formats the server information based on the request.
     * @param request The request command
     * @return The formatted server information as a string
     */
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
                return builder.toString();
            case "INFO":
                builder.append("Server Info:\n");
                serverInfo.getServerDetails().forEach((key, value) -> builder.append(key).append(" = ").append(value).append("\n"));
                return builder.toString();
            case "HELP":
                builder.append("Available Commands:\n");
                serverInfo.getCommands().forEach((key, value) -> builder.append(key).append(" - ").append(value).append("\n"));
                return builder.toString();
            default:
                log.warn("Unknown server info request: {}", request);
                return ResponseMessage.UNKNOWN_REQUEST.getResponse();
        }
    }
}