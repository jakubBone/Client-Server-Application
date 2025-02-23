package com.jakub.bone.server;

import com.jakub.bone.client.command.CommandMessage;

import com.jakub.bone.network.Messenger;
import com.jakub.bone.server.command.ServerCommand;
import com.jakub.bone.server.command.ServerCommandFactory;
import com.jakub.bone.utils.JsonConverter;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;

import java.io.BufferedReader;
import java.io.PrintWriter;

@Log4j2
public class ServerRequestHandler {
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;
    private final ServerCommandFactory factory;
    private final Messenger messenger;

    public ServerRequestHandler(PrintWriter out, BufferedReader in) {
        this.authManager = new AuthService();
        this.userManager = new UserService();
        this.mailService = new MailService();
        this.serverDetails = new ServerDetails();
        this.factory = new ServerCommandFactory(authManager, userManager, mailService, serverDetails);
        this.messenger = new Messenger(out, in);
    }

    public void start() {
        try {
            while (true) {
                String jsonRequest = messenger.receive();
                if (jsonRequest == null || jsonRequest.isEmpty()){
                    System.out.println(jsonRequest);
                    break;
                }
                log.info("Received JSON request: {}", jsonRequest);
                CommandMessage commandMessage = JsonConverter.deserialize(jsonRequest, CommandMessage.class);

                ServerCommand serverCommand = factory.createCommand(commandMessage);

                String result = serverCommand.execute(commandMessage);

                String jsonResponse = JsonConverter.serialize(result) + "\n<<END>>";
                messenger.send(jsonResponse);
            }
        } catch (Exception e) {
            log.error("Error handling client request: {}", e.getMessage());
        }
    }
}
