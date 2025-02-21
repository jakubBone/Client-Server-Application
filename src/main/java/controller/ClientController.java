package controller;

import com.google.gson.Gson;
import client.ClientConnection;
import request.Request;
import request.RequestFactory;
import ui.Screen;
import ui.UserInput;

import java.io.IOException;

public class ClientController {
    private ClientConnection connection;
    private RequestFactory requestFactory;
    private UserInput userInput;
    private Gson gson;

    public ClientController(ClientConnection connection) {
        this.connection = connection;
        this.requestFactory = new RequestFactory(connection);
        this.userInput = new UserInput();
        this.gson = new Gson();
    }

    public void start() throws IOException {
        while (connection.isConnected()) {
            printScreen();
            String command = userInput.getRequest();
            if (command == null || "EXIT".equalsIgnoreCase(command)) {
                connection.disconnect();
                break;
            }
            handleRequest(command);
        }
    }

    private void printScreen() {
        if (!connection.isLoggedIn()) {
            Screen.printMainScreen();
        } else {
            if (connection.isUserAuthorized()) {
                Screen.printAdminScreen();
            } else {
                Screen.printUserScreen();
            }
        }
    }

    private void handleRequest(String command) {
        try {
            Request request = requestFactory.getRequest(command);
            if (request != null) {
                String jsonRequest = gson.toJson(request);
                connection.sendRequest(jsonRequest);
                connection.readResponse();
            } else {
                System.out.println("Niepoprawne polecenie. Spróbuj ponownie.");
            }
        } catch (IOException ex) {
            System.err.println("Błąd podczas przetwarzania polecenia: " + ex.getMessage());
        }
    }
}