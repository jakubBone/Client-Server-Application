
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import operations.OperationResponses;
import utils.JsonConverter;

 /*
  * The ClientConnection class manages the client's connection to the server, allowing sending and receiving of data
  * It handles establishing a connection, sending requests, reading responses, and disconnecting
  */

@Log4j2
@Getter
@Setter
public class ClientConnection {
    private final int PORT_NUMBER = 5000;
    private Socket clientSocket;
    private PrintWriter outToServer;
    private BufferedReader inFromServer;
    private static boolean loggedIn = false;
    private boolean isAuthorized = false;
    public static int connectionAttempts = 0;
    private boolean connected = false;
    private static OperationResponses response;

    /*
     * The ClientConnection class is responsible for managing connections
     * Establishes connections with server, and handles the communication
     */

    public ClientConnection() {
        connectToServer();
    }

    public void connectToServer() {
        try {
            clientSocket = new Socket("localhost", PORT_NUMBER);
            outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            connected = true;
            log.info("Connection with Server established on port {}", PORT_NUMBER);
        } catch (IOException ex) {
            log.error("Failed to establish connection with the server at port {}. Error: {}", PORT_NUMBER, ex.getMessage());
            retryConnection();
        }
    }


    public void retryConnection() {
        if (connectionAttempts >= 2) {
            log.error("Max reconnection attempts reached. Giving up");
            disconnect();
            return;
        }
        try {
            Thread.sleep(2000);
            log.info("Attempting to reconnect to the server...");
            Thread.sleep(5000);
            connectionAttempts++;
            connectToServer();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.warn("Reconnection attempt interrupted", ie);
        }
    }

    public void sendRequest(String request) {
        outToServer.println(request);
        log.info("Sent request to server: {}", request);
    }

    public void readResponse() throws IOException {
        String jsonResponse = null;
        while (!(jsonResponse = inFromServer.readLine()).equals("<<END>>")) {
            String response = JsonConverter.deserializeMessage(jsonResponse);
            checkResponseStatus(response);
            System.out.println(JsonConverter.deserializeMessage(jsonResponse));
        }
    }

    // Checks the login update and role authorization
    public void checkResponseStatus(String response) {
        OperationResponses operationResponse = OperationResponses.fromString(response);

        switch (operationResponse) {
            case LOGIN_SUCCESSFUL:
            case REGISTRATION_SUCCESSFUL:
                loggedIn = true;
                log.info("User logged in successfully");
                break;
            case SUCCESSFULLY_LOGGED_OUT:
                loggedIn = false;
                log.info("User logged out successfully");
                break;
            case LOGIN_FAILED:
                loggedIn = false;
                log.info("Login failed: Incorrect username or password");
                break;
            case REGISTRATION_FAILED:
                loggedIn = false;
                log.info("Registration failed");
                break;
            case OPERATION_SUCCEEDED:
                isAuthorized = true;
                log.info("User authorized for operations");
                break;
            case OPERATION_FAILED:
                isAuthorized = false;
                log.info("User not authorized for operations");
                break;
            default:
                log.warn("Unknown response");
                break;
        }
    }

    public void disconnect() {
        try {
            if (outToServer != null) {
                outToServer.close();
            }
            if (inFromServer != null) {
                inFromServer.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            log.info("Disconnected from server");
        } catch (IOException ex) {
            log.error("Error during disconnection: {}", ex.getMessage());
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }
    public boolean isConnected(){
        return connected;
    }
}