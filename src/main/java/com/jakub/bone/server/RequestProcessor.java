package com.jakub.bone.server;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.repository.MailRepository;
import com.jakub.bone.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final MailRepository mailRepository;

    private final AuthService authService;
    private final UserService userService;
    private final MailService mailService;

    private final ServerInfo serverInfo;
    private final CommandHandlerFactory factory;

    public RequestProcessor(PrintWriter out, BufferedReader in) {
        this.messenger = new Messenger(out, in);
        this.sessionManager = new SessionManager();
        this.userRepository = new UserRepository();
        this.mailRepository = new MailRepository(userRepository);
        this.authService = new AuthService(sessionManager, userRepository);
        this.userService = new UserService(authService, sessionManager, userRepository);
        this.mailService = new MailService(sessionManager, mailRepository);
        this.serverInfo = new ServerInfo();
        this.factory = new CommandHandlerFactory(authService, userService, mailService, serverInfo);

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
