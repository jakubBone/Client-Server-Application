package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.UserManager;
import utils.Screen;
import utils.UserInteraction;


public class Client {
    private static final Logger logger = LogManager.getLogger(Client.class);
    private final int PORT_NUMBER = 5000;
    private Socket clientSocket;
    private PrintWriter outToServer;
    private BufferedReader inFromServer;
    private BufferedReader userInput;
    private static boolean loggedIn = false;


    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client(){
        connectToServer();
        handleServerCommunication();
    }
    public void connectToServer() {
        try {
            clientSocket = new Socket("localhost", PORT_NUMBER);
            logger.info("Connection with Server established");
        } catch (IOException ex){
            logger.error("Error - establishing connection with server.Server", ex);
        }
    }

    public void handleServerCommunication() {
        try {
            outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String request;
                while (!loggedIn) {
                    Screen.printLoginMenu();
                    request = userInput.readLine();
                    if (request == null || request.equalsIgnoreCase("EXIT")) {
                        disconnect();
                        break;
                    }
                    handleLoginRequests(request);
                }

                Screen.printMailBoxMenu();
                request = userInput.readLine();
                if (request == null) {
                    break;
                }
                handleMailRequests(request);
            }
        } catch (IOException ex) {
            logger.error("Error - handling server communication", ex);
        }
    }

    private void handleLoginRequests(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        try {
            switch (request.toUpperCase()) {
                case "REGISTER":
                case "LOGIN":
                    String username = userInteraction.getUsername();
                    String password = userInteraction.getPassword();
                    outToServer.println(request + " " + username + " " + password);
                    readServerResponse();
                    loggedIn = true;
                    break;
                case "HELP":
                    outToServer.println(request);
                    readServerHelpResponse();
                    break;
                default:
                    System.out.println("Incorrect input. please, try again.");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void handleMailRequests(String request) throws IOException {
        UserInteraction userInteraction = new UserInteraction(userInput);
        switch (request.toUpperCase()) {
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                outToServer.println(request + " " + recipient + " " + message);
                readServerResponse();
                break;
            case "READ":
                String boxType = userInteraction.chooseMailBox();
                outToServer.println(request + " " + boxType);
                readServerResponse();
                break;
            case "LOGOUT":
                outToServer.println(request);
                loggedIn = false;
                readServerResponse();
                break;
            default:
                System.out.println("Incorrect input. please, try again.");
        }
    }

    private void readServerHelpResponse() throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while (!(line = inFromServer.readLine()).equals("<<END>>")) {
            response.append(line).append("\n");
        }
        System.out.println("Server response:\n" + response.toString());
        System.out.print("\nType next command: ");
    }

    private void readServerResponse() throws IOException {
        String response = null;
        while (!(response = inFromServer.readLine()).equals("<<END>>")) {
            System.out.println(response);
        }
    }

    public void disconnect() {
        try {
            outToServer.close();
            inFromServer.close();
            clientSocket.close();
            logger.info("Connection stopped");
        } catch (IOException ex) {
            logger.error("Error - disconnecting", ex);
        }
    }
}