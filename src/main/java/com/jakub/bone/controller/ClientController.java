package com.jakub.bone.controller;

import com.jakub.bone.client.command.Command;
import com.jakub.bone.client.command.CommandFactory;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.network.CommunicationGateway;
import com.jakub.bone.network.SocketGateway;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.utils.JsonConverter;
import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;
import com.jakub.bone.application.UserService;
import com.jakub.bone.utils.ResponseStatus;

import java.io.IOException;

public class ClientController {
    private final UserInput userInput;
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final CommandFactory commandFactory;
    private final CommunicationGateway gateway;
    private boolean isLoggedIn = false;
    private boolean isAdmin = false;

    public ClientController() throws IOException {
        this.userInput = new UserInput();
        this.authManager = new AuthService();
        this.userManager = new UserService();
        this.mailService = new MailService();
        this.commandFactory = new CommandFactory(userInput);
        this.gateway = new SocketGateway("localhost", 5000);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printUI();
            try {
                String input = userInput.getRequest();
                if ("EXIT".equalsIgnoreCase(input)) {
                    System.out.println("Exiting application.");
                    gateway.disconnect();
                    running = false;
                    continue;
                }
                Command command = commandFactory.createCommand(input);
                if (command == null) {
                    System.out.println("Unknown command. Try again.");
                    continue;
                }

                CommandMessage commandResult = command.buildCommandMessage();
                String jsonRequest = JsonConverter.serialize(commandResult) + "\n<<END>>";
                gateway.sendMessage(jsonRequest);

                String jsonResponse = gateway.receiveMessage();
                String response = JsonConverter.deserialize(jsonResponse, String.class);

                updateState(response);
                printResponse(response);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void updateState(String response) {
        if (response.equals(ResponseStatus.USER_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
        } else if (response.equals(ResponseStatus.ADMIN_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
            isAdmin = true;
        } else if (response.equals(ResponseStatus.LOGOUT_SUCCEEDED.getResponse())) {
            isLoggedIn = false;
            isAdmin = false;
        }
    }

    private void printUI() {
        if (!isLoggedIn) {
            Screen.printMainScreen();
        } else {
            if (isAdmin) {
                Screen.printAdminScreen();
            } else {
                Screen.printUserScreen();
            }
        }
    }

    private void printResponse(String response){
        Screen.printResponse(response);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.getMessage();
        }
    }
}