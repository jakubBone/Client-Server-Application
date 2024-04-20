package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.Screen;
import utils.UserInteraction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    private ClientConnection connection;
    private BufferedReader userInput;

    public static void main(String[] args) {
        Client client = new Client();
        client.handleServerCommunication();
    }

    public Client() {
        connection = new ClientConnection();
        userInput = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Client application started");
    }

    public void handleServerCommunication() {
        try {
            while(true) {
                String request;
                while (!connection.isLoggedIn()) {
                    Screen.printLoginMenu();
                    request = userInput.readLine();
                    if (request == null || request.equalsIgnoreCase("EXIT")) {
                        connection.disconnect();
                        logger.info("User exited the application");
                        return;
                    }
                    handleLoginRequest(request);
                }
                logger.info("User is logged in, moving to main menu");

                Screen.printMailBoxMenu();
                request = userInput.readLine();
                if (request == null) {
                    logger.info("End of user input stream");
                    break;
                }
                handleMailRequest(request);
            }
        } catch (IOException ex) {
            logger.error("Error in handling server communication: {}", ex.getMessage());
        }
    }

    private void handleLoginRequest(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        String username, password;
        switch (request.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                username = userInteraction.getUsername();
                password = userInteraction.getPassword();
                connection.sendRequest(request + " " + username + " " + password);
                logger.info("User attempted to {}", request);
                connection.readResponse();
                break;
            case "HELP":
                connection.sendRequest(request);
                logger.info("User requested help");
                connection.readResponse();
                break;
            default:
                logger.warn("User entered incorrect input: {}", request);
                System.out.println("Incorrect input. Please, try again");
        }
    }

    private void handleMailRequest(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        switch (request.toUpperCase()) {
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                connection.sendRequest(request + " " + recipient + " " + message);
                logger.info("User sent a message to {}", recipient);
                connection.readResponse();
                break;
            case "MAILBOX":
                String boxOperation = userInteraction.chooseMailBox();
                connection.sendRequest(request + " " + boxOperation);
                logger.info("User accessed their mailbox: {}", boxOperation);
                connection.readResponse();
                break;
            case "UPDATE":
                connection.sendRequest(request);
                logger.info("User attempted to update settings");
                connection.readResponse();
                if(connection.isAuthorized()) {
                    String updateOperation = userInteraction.chooseAccountUpdate();
                    String userToUpdate = userInteraction.chooseUserToUpdate();
                    String newPassword = null;
                    if(updateOperation.equals("PASSWORD")){
                        newPassword = userInteraction.getNewPassword();
                    }
                    connection.sendRequest(updateOperation + " " + userToUpdate + " " + newPassword);
                    logger.info("User updated {} for {}", updateOperation, userToUpdate);
                    connection.readResponse();
                }
                break;
            case "LOGOUT":
                connection.sendRequest(request);
                connection.setLoggedIn(false);
                connection.readResponse();
                logger.info("User logged out");
                break;
            default:
                logger.warn("Incorrect input from user: {}", request);
                System.out.println("Incorrect input. Please, try again");
        }
    }
}