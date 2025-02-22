package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.server.ServerDetails;
import com.jakub.bone.utils.ResponseStatus;

public class ServerDetailsCommand implements ServerCommand {
    private final ServerDetails serverDetails;

    public ServerDetailsCommand(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String command = commandMessage.getCommandType().toUpperCase();
        switch (command) {
            case "UPTIME":
                return serverDetails.getUptime();
            case "INFO":
                return serverDetails.getInfo();
            case "HELP":
                return serverDetails.getHelp();
            default:
                return ResponseStatus.UNKNOWN_REQUEST.getResponse();
        }
    }
}
