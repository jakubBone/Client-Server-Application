import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private Date serverTimeCreation = new Date();
    private final int PORT = 5000;
    private final String VERSION = "1.0.0";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    BufferedReader inFromClient;
    PrintWriter outToClient;

    public static void main(String[] args)  {
        Server server = new Server();
    }

    public Server() {
        establishServerConnection();
        handleClientRequest();
    }

    public void establishServerConnection() {
        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started");
            clientSocket = serverSocket.accept();
            logger.info("Connection with Client established");
        } catch (IOException ex) {
            logger.error("Error establishing server connection", ex);
        }
    }

    public void handleClientRequest(){
        try{
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

                while(true){
                    String request = inFromClient.readLine();
                    if(request.equals("STOP")){
                        disconnect();
                        break;
                    }
                    sendResponse(request, outToClient);
                }
        } catch (IOException ex){
            logger.error("Error handling client request", ex);
        }
    }


    public void sendResponse(String clientRequest, PrintWriter outToClient) throws IOException {
        switch (clientRequest) {
            case "UPTIME":
                Date currentTime = new Date();
                logger.info("Time from server setup: " + (currentTime.getTime() - serverTimeCreation.getTime()));
                outToClient.println("UPTIME returned");
                break;
            case "INFO":
                logger.info("Server version: " + VERSION);
                logger.info("Setup date: " + serverTimeCreation);
                outToClient.println("INFO returned");
                break;
            case "HELP":
                logger.info("Help Menu: ");
                logger.info("UPTIME - opis");
                logger.info("INFO - opis");
                logger.info("HELP - opis");
                logger.info("STOP - opis");
                outToClient.println("HELP returned");
                break;
            default:
                logger.warn("Invalid request");
                outToClient.println("INVALID returned");
        }
    }

    public void disconnect()  {
        try {
            inFromClient.close();
            outToClient.close();
            serverSocket.close();
            clientSocket.close();
            logger.info("Connection stopped");
        } catch (IOException ex) {
            logger.error("Error disconnecting", ex);
        }
    }
}


