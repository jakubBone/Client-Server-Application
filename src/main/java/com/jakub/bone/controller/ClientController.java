package com.jakub.bone.controller;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.client.CommandFactory;

import com.jakub.bone.command.common.CommandDTO;

import com.jakub.bone.utils.Messenger;

import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

import static com.jakub.bone.utils.ResponseStatus.*;


public class ClientController {
    private final UserInput userInput;
    private final CommandFactory commandFactory;
    private final Messenger messenger;
    private boolean isLoggedIn = false;
    private boolean isAdmin = false;

    public ClientController(Messenger messenger) throws IOException {
        this.userInput = new UserInput();
        this.messenger = messenger;
        this.commandFactory = new CommandFactory(userInput);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printUI();
            try {
                String input = userInput.getRequest();
                if ("EXIT".equalsIgnoreCase(input)) {
                    running = false;
                    continue;
                }
                Command command = commandFactory.createCommand(input);
                if (command == null) {
                    System.out.println("Unknown command. Try again.");
                    continue;
                }

                CommandDTO commandDTO = command.buildCommandMessage();
                messenger.send(commandDTO);

                String response = messenger.receive(String.class);

                updateState(response);
                printResponse(response);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void updateState(String response) {
        if (response.equals(USER_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
        } else if (response.equals(ADMIN_LOGIN_SUCCEEDED.getResponse())) {
            isLoggedIn = true;
            isAdmin = true;
        } else if (response.equals(LOGOUT_SUCCEEDED.getResponse())) {
            isLoggedIn = false;
            isAdmin = false;
        } else if (response.equals(USER_SWITCH_SUCCEEDED.getResponse())) {
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