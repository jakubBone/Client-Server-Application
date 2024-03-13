import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

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
            logger.error("Error - establishing server connection", ex);
        }
    }

    public void handleClientRequest(){
        try{
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while((request = inFromClient.readLine().toUpperCase()) != null){
                if(request.equals("STOP")){
                    disconnect();
                    break;
                }
                sendResponse(request, outToClient);
            }
        } catch (IOException ex){
            logger.error("Error - handling client request", ex);
        }
    }

    public void sendResponse(String clientRequest, PrintWriter outToClient) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //Gson gson = new Gson();
        ResponseService responseService = new ResponseService(VERSION, serverTimeCreation);
        String json;

        switch (clientRequest) {
            case "UPTIME":
                json = gson.toJson(responseService.getUptime());
                logger.info("Time from server setup: " + responseService.getUptime());
                break;
            case "INFO":
                json = gson.toJson(responseService.getServerDetails());
                logger.info("Server version: " + VERSION + " / Setup date: " + serverTimeCreation);
                break;
            case "HELP":
                json = gson.toJson(responseService.getCommands());
                logger.info("Commend list displayed");
                break;
            default:
                json = gson.toJson(responseService.getInvalidMessage());
                logger.warn("Invalid input");
        }
        outToClient.println(json);
        System.out.println(json);
    }

    /*public void printJsonAsTree(Gson gson,ResponseService responseService ){
        Map<String, String> serverDetails = responseService.getServerDetails();
        for (Map.Entry<String, String> entry : serverDetails.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String jsonElement = gson.toJsonTree(value).toString();
            System.out.println(key + ": " + jsonElement);
        }

    }*/

    public void disconnect()  {
        try {
            inFromClient.close();
            outToClient.close();
            serverSocket.close();
            clientSocket.close();
            logger.info("Connection stopped");
        } catch (IOException ex) {
            logger.error("Error - disconnecting", ex);
        }
    }
}