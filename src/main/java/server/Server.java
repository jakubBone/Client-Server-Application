package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import mail.Mail;
import mail.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.User;
import user.UserManager;

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
    private MailService mailService;
    private ServerInfoService helperService;


    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        establishServerConnection();
        handleClientRequest();
    }

    public void establishServerConnection() {
        try {
            helperService = new ServerInfoService(VERSION, serverTimeCreation);
            serverSocket = new ServerSocket(PORT);
            logger.info("Server started");
            clientSocket = serverSocket.accept();
            logger.info("Connection with Client established");
        } catch (IOException ex) {
            logger.error("Error - establishing server connection", ex);
        }
    }

    public void handleClientRequest() {
        /*try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true)) {
            // reszta kodu
        }*/


        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            userManager = new UserManager();
            mailService = new MailService();
            helperService = new ServerInfoService(VERSION, serverTimeCreation);

            String request;
            while ((request = inFromClient.readLine()) != null) {
                if (request.equalsIgnoreCase("EXIT")) {
                    disconnect();
                    break;
                }

                String[] parts = request.split(" ", 3);
                String command = parts[0].toUpperCase();
                switch (command) {
                    case "REGISTER":
                    case "LOGIN":
                        String username = parts[1];
                        String password = parts[2];
                        handleAuthentication(command, username, password);
                        break;
                    case "HELP":
                        helperService.handleHelpRequest(command, outToClient);
                        break;
                    case "WRITE":
                        String recipient = parts[1];
                        String message = parts[2];
                        handleWrite(recipient, message);
                        break;
                    case "READ":
                        String boxType = parts[1];
                        handleRead(boxType);
                        break;
                    case "LOGOUT":
                        handleLogout();
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

    private void handleWrite(String recipient, String message) throws IOException {
        User recipientUser = userManager.getRecipientByUsername(recipient);
        if (recipientUser != null) {
            if(recipientUser.getMailBox().ifBoxFull()){
                outToClient.println("Sending failed: Recipient MailBox is full \n<<END>>");
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    outToClient.println("Mail sent successfully\n<<END>>");
                } else {
                    outToClient.println("Sending failed: Message too long (max 255 signs) \n<<END>>");
                }
            }
        } else {
            outToClient.println("Sending failed: Recipient not found\n<<END>>");
        }
    }
    private void handleRead(String boxType) throws IOException {
        List<Mail> mailsToRead = mailService.getMailsToRead(boxType);
        for (Mail mail : mailsToRead) {
            System.out.println("Sendi " + mail.getSender());
            outToClient.println("From: " + mail.getSender().getUsername() + "\n Message: " + mail.getMessage());
        }
        mailService.markMailsAsRead(mailsToRead); /// tutaj UNREAD zawsze jest Null
        outToClient.println("<<END>>");

    }

    private void handleLogout() {
        userManager.logoutCurrentUser();
        outToClient.println("Successfully logged out\n<<END>>");
    }


    public void disconnect() {
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