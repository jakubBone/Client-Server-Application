package server;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

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

@Log4j2
@Getter
@Setter
public class ServerConnection {
    private int port;
    public static Date serverTimeCreation;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;

    public ServerConnection(int port) {
        this.port = port;
    }

    public void startServer(){
        try{
            log.info("Starting server on port {}", port);
            serverTimeCreation = new Date();
            serverSocket = new ServerSocket(port);
            log.info("Server socket created on port {}", port);
            clientSocket = serverSocket.accept();
            log.info("Connection with client established");
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex){
            log.error("Error starting server on port {}: {}", port, ex.getMessage());
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
            log.info("Closing server and client connections");
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
            log.info("Server and client connections closed");
        } catch (IOException ex) {
            log.error("Error closing connections: {}", ex.getMessage());
        }
    }
}
