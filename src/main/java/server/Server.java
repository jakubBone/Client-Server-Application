package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.UserAuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.User;
import user.UserManager;
import utils.Message;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private Date serverTimeCreation = new Date();
    private final int PORT = 5000;
    private final String VERSION = "1.0.0";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader inFromClient;
    private PrintWriter outToClient;
    private UserManager userManager;


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
            String response;

            while((request = inFromClient.readLine()) != null){
                if(request.equals("EXIT")){
                    disconnect();
                    break;
                }

                if(request.equals("REGISTER") || request.equals("LOGIN")){
                    response = "Username:" + "\n<<END>>\n";
                    outToClient.println(response);
                    String username = inFromClient.readLine();
                    outToClient.println("Password:"+ "\n<<END>>\n");
                    String password = inFromClient.readLine();
                    handleLoginRequest(request, outToClient, username, password);
                } else {
                    sendHelpRequest(request, outToClient);
                }
            }
        } catch (IOException ex){
            logger.error("Error - handling client request", ex);
        }
    }

    public void handleLoginRequest(String clientRequest, PrintWriter outToClient, String username, String password) {
       try{
           userManager = new UserManager();
           switch (clientRequest) {
               case "REGISTER":
                   userManager.register(username,password);
                   logger.info("REGISTER success");
                   break;
               case "LOGIN":
                   userManager.login(username, password);
                   logger.info("LOGIN success");
                   break;
               case "HELP":
                   sendHelpRequest(clientRequest, outToClient);
                   logger.info("HELP success");
                   break;
               default:
                   logger.warn("Invalid input");
           }
       } catch (UserAuthenticationException exception){
           exception.getMessage();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

    }

    public void sendHelpRequest(String clientRequest, PrintWriter outToClient) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Message response = new Message(VERSION, serverTimeCreation);
        String json;
        switch (clientRequest) {
            case "UPTIME":
                json = gson.toJson(response.getUptime());
                logger.info("Time from server setup: " + response.getUptime());
                break;
            case "INFO":
                json = gson.toJson(response.getServerDetails());
                logger.info("server.Server version: " + VERSION + " / Setup date: " + serverTimeCreation);
                break;
            case "HELP":
                json = gson.toJson(response.getCommands());
                logger.info("Commend list displayed");
                break;
            default:
                json = gson.toJson(response.getInvalidMessage());
                logger.warn("Invalid input");
        }
        json += "\n<<END>>\n";
        outToClient.println(json);
    }

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