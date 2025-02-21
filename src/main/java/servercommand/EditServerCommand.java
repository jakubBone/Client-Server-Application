package servercommand;

import command.CommandMessage;

import java.util.Map;

public class EditServerCommand implements ServerCommand{

    @Override
    public String execute(CommandMessage commandMessage) {
        String commandType = commandMessage.getCommandType().toUpperCase();
        StringBuilder builder = new StringBuilder();
        switch (commandType) {
            case "UPTIME":
                Map<String, Long> uptime = serverDetails.getUptime();
                builder.append("Uptime: ")
                        .append(uptime.get("Days")).append(" days, ")
                        .append(uptime.get("Hours")).append(" hours, ")
                        .append(uptime.get("Minutes")).append(" minutes, ")
                        .append(uptime.get("Seconds")).append(" seconds");
                break;
            case "INFO":
                Map<String, String> details = serverDetails.getServerDetails();
                details.forEach((key, value) -> builder.append(key).append(": ").append(value).append("\n"));
                break;
            case "HELP":
                Map<String, String> commands = serverDetails.getCommands();
                commands.forEach((key, value) -> builder.append(key).append(" - ").append(value).append("\n"));
                break;
            default:
                builder.append("Unknown server command");
        }
        return builder.toString();
}
