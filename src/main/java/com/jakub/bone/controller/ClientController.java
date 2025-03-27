package com.jakub.bone.controller;

import com.jakub.bone.command.client.*;
import com.jakub.bone.command.common.Command;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.ui.ConsolerReader;
import com.jakub.bone.utils.Messenger;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import static com.jakub.bone.ui.Screen.printClientUI;
import static com.jakub.bone.ui.Screen.printResponse;
import static com.jakub.bone.utils.ResponseStatus.*;

@Log4j2
public class ClientController {
    private final ConsolerReader reader;
    private final CommandFactory commandFactory;
    private final Messenger messenger;
    private boolean isLoggedIn = false;
    private boolean isAuthorized = false;

    public ClientController(Messenger messenger) throws IOException {
        this.reader = new ConsolerReader();
        this.messenger = messenger;
        this.commandFactory = new CommandFactory();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printClientUI(isLoggedIn, isAuthorized);
            try {
                String request = reader.getRequest();
                if ("EXIT".equalsIgnoreCase(request)) {
                    running = false;
                    continue;
                }

                Command command = switch (request.toUpperCase()) {
                    case "LOGIN", "REGISTER" -> new AuthCommand(request);
                    case "LOGOUT" -> new LogoutCommand();
                    case "UPTIME", "INFO", "HELP" -> new ServerInfoCommand(request);
                    case "NEW" -> new NewMailCommand(reader);
                    case "READ" -> new ReadMailCommand(reader);
                    case "SEARCH" -> new ElasticSearchCommand(reader);
                    case "DELETE" -> new DeleteMailCommand(reader);
                    case "EDIT" -> new EditUserCommand(reader);
                    default -> {
                        log.warn("Unknown operation: {}", request);
                        yield null;
                    }
                };

                if (command == null) {
                    System.out.println("Unknown command. Try again");
                    continue;
                }

                CommandDTO commandDTO = commandFactory.createCommand(command);
                messenger.send(commandDTO);

                String response = messenger.receive(String.class);

                updateState(response);
                printResponse(response);

            } catch (IOException e) {
                log.error("Error processing command: {}", e.getMessage());
                System.err.println("An error occurred while processing your command: " + e.getMessage());
            }
        }
    }

    private void updateState(String response) {
        if (response.equals(USER_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
        } else if (response.equals(ADMIN_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
            isAuthorized = true;
        } else if (response.equals(LOGOUT_SUCCEEDED.getResponse())) {
            isLoggedIn = false;
            isAuthorized = false;
        } else if (response.equals(USER_SWITCH_SUCCEEDED.getResponse())) {
            isAuthorized = false;
        }
    }
}