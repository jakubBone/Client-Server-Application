package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.server.ServerDetails;
import com.jakub.bone.utils.ResponseStatus;

public class ServerInfoHandler implements CommandHandler {
    private final ServerDetails serverDetails;

    public ServerInfoHandler(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String command = commandDTO.getCommandType().toUpperCase();
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
