package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

 /*
  * The ServerConnectionHandler class is responsible for managing server connections, including starting the server
  * Establishes connections with client, and handle the communication
  */

public class ServerConnectionHandler {
    private static final Logger logger = LogManager.getLogger(ServerConnectionHandler.class);
    private int port;
    public static Date serverTimeCreation;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;

    public ServerConnectionHandler(int port) {
        this.port = port;
    }

    public void startServer(){
            try{
                serverTimeCreation = new Date();
                serverSocket = new ServerSocket(port);
                logger.info("Server started on port {}", port);
                clientSocket = serverSocket.accept();
                logger.info("Connection with client established");
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException ex){
                logger.error("Error starting server on port {}: {}", port, ex.getMessage());
            }
    }

    public BufferedReader getInFromClient() {
        return inFromClient;
    }

    public PrintWriter getOutToClient() {
        return outToClient;
    }

    public void closeConnections() {
        try {
            if (inFromClient != null) {
                inFromClient.close();
            }
            if (outToClient != null) {
                outToClient.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            logger.info("Server and client connections closed");
        } catch (IOException ex) {
            logger.error("Error closing connections: {}", ex.getMessage());
        }
    }
}
