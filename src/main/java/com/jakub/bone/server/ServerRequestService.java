package com.jakub.bone.server;

import java.io.IOException;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.network.CommunicationGateway;

import com.jakub.bone.server.command.ServerCommand;
import com.jakub.bone.server.command.ServerCommandFactory;
import com.jakub.bone.utils.JsonConverter;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;

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

                // 2. Deserializujemy JSON do CommandMessage (zamiast do String)
                CommandMessage commandMessage = JsonConverter.deserialize(jsonRequest, CommandMessage.class);

                // 3. Tworzymy odpowiedni ServerCommand na podstawie commandType
                //    (dodaj w ServerCommandFactory metodę createCommand(CommandMessage) lub wywołuj createCommand(commandMessage.getCommandType()))
                ServerCommand serverCommand = factory.createCommand(commandMessage);

                // 4. Wykonujemy logikę i otrzymujemy wynik
                String result = serverCommand.execute(commandMessage);

                // 5. Wysyłamy wynik z powrotem do klienta, serializując go do JSON
                String jsonResponse = JsonConverter.serialize(result) + "\n<<END>>";
                gateway.sendMessage(jsonResponse);
            }
        } catch (Exception e) {
            log.error("Error handling client request: {}", e.getMessage());
        } finally {
            gateway.disconnect();
        }
    }
}
