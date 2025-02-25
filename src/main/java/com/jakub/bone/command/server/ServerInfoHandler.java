package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.server.ServerInfo;
import com.jakub.bone.utils.ResponseStatus;
import lombok.extern.log4j.Log4j2;

import static com.jakub.bone.utils.ResponseStatus.UNKNOWN_REQUEST;

@Log4j2
public class ServerInfoHandler implements CommandHandler {
    private final ServerInfo serverInfo;

    public ServerInfoHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String command = commandDTO.getCommandType().toUpperCase();
        switch (command) {
            case "UPTIME":
                return serverInfo.getUptime();
            case "INFO":
                return serverInfo.getInfo();
            case "HELP":
                return serverInfo.getHelp();
            default:
                log.warn("Unknown operation: {}", command);
                return UNKNOWN_REQUEST.getResponse();
        }
    }
}
