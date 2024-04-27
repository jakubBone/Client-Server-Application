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
    private Request requestType;
    private String jsonRequest;

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
            while(true) {
                String request;
                while (!connection.isLoggedIn()) {
                    Screen.printLoginMenu(); // Display login menu
                    request = userInput.readLine();
                    if (request == null || request.equalsIgnoreCase("EXIT")) {
                        connection.disconnect();
                        log.info("User exited the application");
                        return;
                    }
                    handleLoginRequest(request); // Handle login-related requests
                }
                log.info("User is logged in, moving to main menu");

                Screen.printMailBoxMenu(); // Display mailbox menu
                request = userInput.readLine();
                if (request == null) {
                    log.info("End of user input stream");
                    break;
                }
                handleMailRequest(request); // Handle mailbox-related requests
            }
        } catch (IOException ex) {
            //ogger.error("Error in handling server communication: {}", ex.getMessage());
        }
    }


    /*
     * Handles user login requests (e.g., REGISTER, LOGIN, HELP)
     * HELP - displays help menu
     */
    private void handleLoginRequest(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        switch (request.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                String username = userInteraction.getUsername();
                String password = userInteraction.getPassword();
                requestType = new LoginRegisterRequest(request, username, password);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User attempted to {}", request);
                connection.readResponse();
                break;
            case "HELP":
                requestType = new HelpRequest(request);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User requested help");
                connection.readResponse();
                break;
            default:
                log.warn("User entered incorrect input: {}", request);
                System.out.println("Incorrect input. Please, try again");
        }
    }

    /*
     * Handles mailbox and related requests (WRITE, MAILBOX, UPDATE, LOGOUT)
     * WRITE - mail sending
     * MAILBOX - mail reading and deleting
     * UPDATE - password changing and account removing required ADMIN role
     */
    private void handleMailRequest(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        switch (request.toUpperCase()) {
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                System.out.println(request);
                System.out.println(message);
                requestType = new WriteRequest(request, recipient, message);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User sent a message to {}", recipient);
                connection.readResponse();
                break;
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                requestType = new MailBoxRequest(request, boxOperation, mailbox);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User accessed their mailbox: {}", boxOperation);
                connection.readResponse();
                break;
            case "UPDATE":
                requestType = new UpdateRequest(request);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                log.info("User attempted to update settings");
                connection.readResponse();
                if(connection.isAuthorized()) {
                    String updateOperation = userInteraction.chooseAccountUpdate();
                    String userToUpdate = userInteraction.chooseUserToUpdate();
                    String newPassword = null;
                    if(updateOperation.equals("PASSWORD")){
                        newPassword = userInteraction.getNewPassword();
                    }
                    requestType = new UpdateRequest(updateOperation, userToUpdate, newPassword);
                    jsonRequest = gson.toJson(requestType);
                    connection.sendRequest(jsonRequest);
                    log.info("User updated {} for {}", updateOperation, userToUpdate);
                    connection.readResponse();
                }
                break;
            case "LOGOUT":
                log.info("User attempted to log out");
                requestType = new LogoutRequest(request);
                jsonRequest = gson.toJson(requestType);
                connection.sendRequest(jsonRequest);
                connection.setLoggedIn(false);
                connection.readResponse();
                break;
            default:
                log.warn("Incorrect input from user: {}", request);
                System.out.println("Incorrect input. Please, try again");
        }
    }
}