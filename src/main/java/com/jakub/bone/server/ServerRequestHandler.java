package com.jakub.bone.server;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.network.Messenger;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.command.server.CommandHandlerFactory;
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
    private final CommandHandlerFactory factory;
    private final Messenger messenger;

    public ServerRequestHandler(PrintWriter out, BufferedReader in) {
        this.authManager = new AuthService();
        this.userManager = new UserService();
        this.mailService = new MailService();
        this.serverDetails = new ServerDetails();
        this.factory = new CommandHandlerFactory(authManager, userManager, mailService, serverDetails);
        this.messenger = new Messenger(out, in);
    }

    public void start() {
        try {
            while (true) {
                CommandDTO commandDTO = messenger.receive(CommandDTO.class);;
                if (commandDTO == null) {
                    break;
                }
                log.info("Received JSON request: {}", commandDTO);

                CommandHandler commandHandler = factory.createCommand(commandDTO);
                String response = commandHandler.execute(commandDTO);

                messenger.send(response);
            }
        } catch (Exception e) {
            log.error("Error handling client request: {}", e.getMessage());
        }
    }
}
