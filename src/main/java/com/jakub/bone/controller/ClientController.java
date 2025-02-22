package com.jakub.bone.controller;

import com.jakub.bone.client.command.Command;
import com.jakub.bone.client.command.CommandFactory;
import com.jakub.bone.application.AuthService;
import com.jakub.bone.application.MailService;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.network.CommunicationGateway;
import com.jakub.bone.network.SocketGateway;
import com.jakub.bone.utils.JsonConverter;
import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;
import com.jakub.bone.application.UserService;

import java.io.IOException;

public class ClientController {
    private final UserInput userInput;
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final CommandFactory commandFactory;
    private final CommunicationGateway gateway;

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

                printResponse(response);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void printUI() {
        if (!userManager.isLoggedIn()) {
            Screen.printMainScreen();
        } else if (userManager.isUserAdmin()) {
            Screen.printAdminScreen();
        } else {
            Screen.printUserScreen();
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