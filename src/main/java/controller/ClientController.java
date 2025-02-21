package controller;

import command.Command;
import command.CommandFactory;
import service.AuthService;
import service.MailService;

import network.CommunicationGateway;
import network.SocketGateway;
import utils.JsonConverter;
import ui.Screen;
import ui.UserInput;
import service.UserService;

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
                } else {
                    String commandResult = command.buildCommandMessage()
                    String jsonRequest = JsonConverter.serialize(commandResult) + "\n<<END>>";
                    gateway.sendMessage(jsonRequest);
                    String jsonResponse = gateway.receiveMessage();
                    String response = JsonConverter.deserialize(jsonResponse);
                    System.out.println("Server Response: " + response);
                }
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
}