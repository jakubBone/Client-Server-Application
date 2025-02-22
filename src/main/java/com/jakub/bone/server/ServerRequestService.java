package com.jakub.bone.server;

import java.io.IOException;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.network.CommunicationGateway;

import com.jakub.bone.server.command.AuthServerCommand;
import com.jakub.bone.server.command.ServerCommand;
import com.jakub.bone.server.command.ServerCommandFactory;
import com.jakub.bone.utils.JsonConverter;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.service.AuthService;
import com.jakub.bone.application.service.MailService;
import com.jakub.bone.application.service.UserService;

@Log4j2
public class ServerRequestService {
    private final CommunicationGateway gateway;
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;
    private final ServerCommandFactory factory;


    public ServerRequestService(CommunicationGateway gateway) {
        this.gateway = gateway;
        this.authManager = new AuthService();
        this.userManager = new UserService();
        this.mailService = new MailService();
        this.serverDetails = new ServerDetails();
        this.factory = new ServerCommandFactory(authManager, userManager, mailService, serverDetails);
    }

    public void handleClientRequest() {
        try {
            while (true) {
                String jsonRequest = gateway.receiveMessage();
                if (jsonRequest == null || jsonRequest.isEmpty()) break;
                log.info("Received JSON request: {}", jsonRequest);

                CommandMessage commandMessage = JsonConverter.deserialize(jsonRequest);
                ServerCommand serverCommand = factory.createCommand(commandMessage);
                // ?????

                /*String jsonRequest = gateway.receiveMessage();
                if (jsonRequest == null || jsonRequest.isEmpty()) break;
                log.info("Received JSON request: {}", jsonRequest);
                String request = JsonConverter.deserialize(jsonRequest, String.class);
                String response = processRequest(request)
                String jsonResponse = JsonConverter.serialize(response) + "\n<<END>>";
                gateway.sendMessage(jsonResponse);*/
            }
        } catch (IOException e) {
            log.error("Error handling client request: {}", e.getMessage());
        } finally {
            gateway.disconnect();
        }
    }
}
