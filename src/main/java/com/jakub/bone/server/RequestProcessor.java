package com.jakub.bone.server;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.session.SessionManager;
import com.jakub.bone.utils.Messenger;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.command.server.CommandHandlerFactory;
import lombok.extern.log4j.Log4j2;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;
import com.jakub.bone.application.UserService;

import java.io.BufferedReader;
import java.io.PrintWriter;

@Log4j2
public class RequestProcessor {
    private final Messenger messenger;
    private final SessionManager sessionManager;
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerInfo serverInfo;
    private final CommandHandlerFactory factory;

    public RequestProcessor(PrintWriter out, BufferedReader in) {
        this.messenger = new Messenger(out, in);
        this.sessionManager = new SessionManager();
        this.authManager = new AuthService(sessionManager);
        this.userManager = new UserService(authManager);
        this.mailService = new MailService(sessionManager);
        this.serverInfo = new ServerInfo();
        this.factory = new CommandHandlerFactory(authManager, userManager, mailService, serverInfo);

    }

    public void start() {
        try {
            while (true) {
                CommandDTO commandDTO = messenger.receive(CommandDTO.class);;
                if (commandDTO == null) {
                    break;
                }
                log.debug("Received JSON request: {}", commandDTO);
                CommandHandler commandHandler = factory.createHandler(commandDTO);
                String response = commandHandler.execute(commandDTO);

                messenger.send(response);
            }
        } catch (Exception e) {
            log.error("Error while handling client request: {}", e.getMessage());

        }
    }
}
