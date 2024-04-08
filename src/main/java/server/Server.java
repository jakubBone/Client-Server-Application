package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.User;
import user.UserManager;
import utils.Message;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static Date serverTimeCreation = new Date();
    private final int PORT = 5000;
    private final static String VERSION = "1.0.0";
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
    public void handleClientRequest() {
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            userManager = new UserManager();
            String request;
            while ((request = inFromClient.readLine()) != null) {
                if (request.equalsIgnoreCase("EXIT")) {
                    disconnect();
                    break;
                }

                String[] parts = request.split(" ", 3); // format: COMMAND username password
                String command = parts[0].toUpperCase();

                switch (command.toUpperCase()) {
                    case "REGISTER":
                    case "LOGIN":
                        String username = parts[1];
                        String password = parts[2];
                        logger.info("register");
                        handleAuthentication(command, username, password);
                        logger.info("after register");
                        break;
                    case "HELP":
                        logger.info("before help");
                        handleHelpRequest(command, outToClient);
                        logger.info("after help");
                        break;
                    case "WRITE":
                    case "READ":
                    case "LOGOUT":
                        //handleMailRequests(request);
                        break;
                    default:
                        System.out.println("???");
                        break;
                }
            }
        } catch (IOException ex) {
            logger.error("Error - handling client request", ex);
        }
    }
    public void handleAuthentication(String request, String username, String password) throws IOException {
        switch (request) {
            case "REGISTER":
                userManager.register(username, password);
                outToClient.println("Registration successful\n<<END>>");
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    outToClient.println("Login successful\n<<END>>");
                    user.setUserLoggedIn(true);
                    UserManager.currentLoggedInUser = user;
                } else {
                    outToClient.println("Login failed: Incorrect username or password\n<<END>>");
                }
                break;
        }
        logger.info("out from handleAuthentication");
    }


    /*private void handleMailRequests(String request) throws IOException  {
        String[] parts = request.split(" ", 2);
        String command = parts[0].toUpperCase();
        String userDetails = parts.length > 1 ? parts[1] : null; // could contain recipient, message etc.

        switch (command) {
            case "WRITE":
                List<User> recipientsList = userManager.getUsersList();
                outToClient.println(recipientsList);
                // userDetails could contain recipient and message eg. "recipientName Mail content here"
                String[] writeParts = userDetails.split(" ", 2);
                String recipientName = writeParts[0];
                User recipient = userManager.getRecipientByUsername(recipientName);
                String mailContent = writeParts[1];
                mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipient , mailContent));
                outToClient.println("Mail sent successfully\n<<END>>");
                break;
            case "READ":
                List<Mail> mails = mailService.readMails(UserManager.currentLoggedInUser);
                for (Mail mail : mails) {
                    outToClient.println("From: " + mail.getSender().getUsername() + " \n Message: " + mail.getMessage());
                    mail.markAsRead();
                }
                outToClient.println("<<END>>");
                break;
            case "LOGOUT":
                outToClient.println("Successfully logged out\n<<END>>");
                UserManager.currentLoggedInUser = null;
                break;
        }
    }*/

    public static void handleHelpRequest(String reguest, PrintWriter outToClient) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Message response = new Message(VERSION, serverTimeCreation);
        String json;
        switch (reguest) {
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
                logger.warn("Invalid input ---------");
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