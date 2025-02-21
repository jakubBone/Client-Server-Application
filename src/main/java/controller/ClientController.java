package controller;

import com.google.gson.Gson;
import client.ClientConnection;
import command.CommandDispatcher;
import mail.MailService;
import request.Request;
import request.RequestFactory;
import ui.Screen;
import ui.UserInput;
import user.manager.AuthManager;
import user.manager.UserManager;

import java.io.IOException;

public class ClientController {
    private final UserInput userInput;
    private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;
    private final CommandDispatcher dispatcher;

    public ClientController() {
        this.userInput = new UserInput();
        this.authManager = new AuthManager();
        this.userManager = new UserManager();
        this.mailService = new MailService();
        this.dispatcher = new CommandDispatcher(userInput, authManager, userManager, mailService);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printScreen();
            String input = userInput.getRequest();
            String result = dispatcher.dispatch(input);
            if ("EXIT".equals(result)) {
                System.out.println("Wychodzenie z aplikacji...");
                running = false;
            } else {
                System.out.println(result);
            }
        }
    }

    private void printScreen() {
        // Wyświetlanie menu zależnie od stanu zalogowania oraz roli użytkownika
        if (!userManager.isLoggedIn()) {
            Screen.printMainScreen();
        } else {
            if (userManager.isUserAdmin()) {
                Screen.printAdminScreen();
            } else {
                Screen.printUserScreen();
            }
        }
    }
}