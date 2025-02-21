package response.server;

import command.CommandMessage;
import response.Response;
import request.Request;
import server.ServerDetails;
import servercommand.ServerCommand;
import utils.ResponseStatus;

public class UptimeServerCommand implements ServerCommand {
    private final ServerDetails serverDetails;
    public UptimeServerCommand(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
    }
    @Override
    public String execute(CommandMessage commandMessage) {
        String serverRequest = commandMessage.getCommandType();
        StringBuilder builder = new StringBuilder();
        switch (serverRequest) {
            case "UPTIME":
                builder.append("Uptime:\n");
                builder.append(String.format("%d days, %d hours, %d minutes, %d seconds",
                        serverDetails.getUptime().get("Days"),
                        serverDetails.getUptime().get("Hours"),
                        serverDetails.getUptime().get("Minutes"),
                        serverDetails.getUptime().get("Seconds")));
                return builder.toString();
            case "INFO":
                builder.append("Server Info:\n");
                serverDetails.getServerDetails().forEach((key, value) -> builder.append(key).append(" = ").append(value).append("\n"));
                return builder.toString();
            case "HELP":
                builder.append("Available Commands:\n");
                serverDetails.getCommands().forEach((key, value) -> builder.append(key).append(" - ").append(value).append("\n"));
                return builder.toString();
            default:
                return ResponseStatus.UNKNOWN_REQUEST.getResponse();
        }
    }
}
