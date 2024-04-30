package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import request.*;
import utils.Screen;
import utils.UserInteraction;

 /*
  * This class represents a simple client application for server communication.
  * It allows users to log in, send requests, and interact with a mailbox
  */

@Log4j2
public class Client {
    private ClientConnection connection;
    private BufferedReader userInput;
    private static Gson gson;

    public static void main(String[] args) {
        Client client = new Client();
        gson = new Gson();
        client.handleServerCommunication();
    }

    public Client() {
        connection = new ClientConnection();
        userInput = new BufferedReader(new InputStreamReader(System.in));
        log.info("Client application started");
    }

    /*
     * Handles communication with the server.
     * Includes user login, mailbox interaction, and handling other requests
     */
    public void handleServerCommunication() {
        try {
            while(true){
                if(!connection.isLoggedIn()) {
                    Screen.printLoginMenu();
                } else {
                    Screen.printMailBoxMenu();
                }
                String request = userInput.readLine();
                if (request == null || request.equalsIgnoreCase("EXIT")) {
                    connection.disconnect();
                    log.info("User exited the application");
                    return;
                }
                handleRequest(request); // Handle login-related requests
            }
        } catch (IOException ex) {
            log.error("Error in handling server communication: {}", ex.getMessage());
        }
    }

    /*
     * Handles user login requests (e.g., REGISTER, LOGIN, HELP)
     * HELP - displays help menu
     */
    private void handleRequest(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        RequestFactory factory = new RequestFactory();

        Request requestType = factory.createRequest(request, userInteraction);

        if (requestType != null) {
            String jsonRequest = gson.toJson(requestType);
            connection.sendRequest(jsonRequest);
            log.info("User attempted to {}", request);
            connection.readResponse();
            if(connection.isAuthorized()) {
                requestType = factory.extendUpdateRequest(userInteraction);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User updated {} for {}", requestType.getUpdateOperation(), requestType.getUserToUpdate());
                connection.readResponse();
                connection.setAuthorized(false);
            }
        } else {
            log.warn("Incorrect input from user: {}", request);
            System.out.println("Incorrect input. Please, try again");
        }
    }
}