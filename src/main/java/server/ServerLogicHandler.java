package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mail.Mail;
import mail.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.Admin;
import user.User;
import user.UserManager;
import utils.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ServerLogicHandler {
    private static final Logger logger = LogManager.getLogger(ServerLogicHandler.class);
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private final MailService mailService;
    private final ServerInfo serverInfo;
    private JsonConverter jsonResponse;
    private boolean isAuthorized = false;
    

    public ServerLogicHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.mailService = new MailService();
        this.serverInfo = new ServerInfo();
    }

    public void handleClientRequest() {
        logger.info("Starting to handle client requests.");
        String request = null;
        try {
            while ((request = inFromClient.readLine()) != null) {
                String[] parts = request.split(" ", 3);
                String command = parts[0].toUpperCase();
                logger.info("Handling command: {}", command);
                switch (command) {
                    case "REGISTER":
                    case "LOGIN":
                        handleAuthentication(command, parts[1], parts[2]);
                        break;
                    case "HELP":
                        handleHelpRequest(command);
                        break;
                    case "WRITE":
                        handleWrite(parts[1], parts[2]);
                        break;
                    case "MAILBOX":
                        if(parts[1].equals("READ")){
                            handleMailbox(parts[2]);
                        } else {
                            handleEmpty(parts[2]);
                        }
                        break;
                    case "UPDATE":
                        handleUpdateRequest();
                        break;
                    case "LOGOUT":
                        handleLogout();
                        break;
                    case "EXIT":
                        return;
                }
                logger.info("Completed authentication command: {}", command);
            }
        } catch (IOException ex) {
            logger.error("IOException occurred while processing the request: {}. Error: {}", request, ex.getMessage());
        }
    }

    private void handleUpdateRequest() throws IOException{
        String response = null;
        if(!userManager.isAdmin()){
            logger.warn("Unauthorized attempt to update by non-admin user.");
            response = "Operation failed: Not authorized";
        } else {
            logger.info("Authorized update attempt by admin user");
            isAuthorized = true;
            response = "Operation succeeded: Authorized";
            sendResponse(response);
            String updateRequest = inFromClient.readLine();
            String[] updateOperationParts = updateRequest.split(" ", 3);
            handleUpdate(updateOperationParts[0], updateOperationParts[1], updateOperationParts[2]);
        }
    }

    private void handleUpdate(String updateOperation, String userToUpdate, String newPassword) throws IOException {
        User searchedUser = userManager.findUserOnTheList(userToUpdate);
        Admin admin = new Admin();
        String response = null;
        if(!(searchedUser == null)) {
            switch (updateOperation.toUpperCase()) {
                case "PASSWORD":
                    admin.changePassword(searchedUser, newPassword);
                    response = searchedUser.getUsername() + " password change successful";
                    logger.info("Password changed successfully for user: {}", searchedUser.getUsername());
                    break;
                case "DELETE":
                    if(searchedUser.getRole().equals(User.Role.ADMIN)){
                        response = "Operation failed: admin account cannot be deleted";
                        logger.warn("Attempted to impossible delete admin account for user: {}", searchedUser.getUsername());
                    } else {
                        admin.deleteUser(searchedUser);
                        response = searchedUser.getUsername() + " account deletion successful";
                        logger.info("User account deleted successfully: {}", searchedUser.getUsername());
                    }
                    break;
            }
        } else {
            response = "Update failed: " + userToUpdate + " not found";
            logger.warn("Failed to find user for update: {}", userToUpdate);
        }
        sendResponse(response);
    }

    public void handleAuthentication(String command, String username, String password) throws IOException {
        String response = null;
        switch (command) {
            case "REGISTER":
                userManager.register(username, password);
                logger.info("Registration attempted for user: {}", username);
                response = "Registration successful";
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    logger.info("User logged in successfully: {}", username);
                    UserManager.currentLoggedInUser = user;
                    response = "Login successful";
                } else {
                    logger.warn("Login attempt failed for user: {}", username);
                    response = "Login failed: Incorrect username or password";
                }
                break;
        }
        sendResponse(response);
    }

    public void handleHelpRequest(String request) {
        logger.info("Received help request: {}", request);
        String response = null;
        switch (request) {
            case "UPTIME":
                response = serverInfo.getUptime().toString();
                logger.info("UPTIME requested, response: {}", response);
                break;
            case "INFO":
                response = serverInfo.getServerDetails().toString();
                logger.info("INFO requested, response: {}", response);
                break;
            case "HELP":
                response = serverInfo.getCommands().toString();
                logger.info("HELP requested, response: {}", response);
                break;
        }
        sendResponse(response);
    }

    private void handleWrite(String recipient, String message) throws IOException {
        User recipientUser = userManager.getRecipientByUsername(recipient);
        String response = null;
        if (recipientUser != null) {
            if(recipientUser.getMailBox().ifBoxFull()){
                logger.warn("Mail sending failed, recipient's mailbox is full: {}", recipient);
                response = "Sending failed: Recipient's mailbox is full";
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    logger.info("Mail sent successfully to: {}", recipient);
                    response = "Mail sent successfully";
                } else {
                    logger.warn("Mail sending failed, message too long for recipient: {}", recipient);
                    response = "Sending failed: Message too long (maximum 255 characters)";
                }
            }
        } else {
            logger.warn("Mail sending failed, recipient not found: {}", recipient);
            response = "Sending failed: Recipient not found";
        }
        sendResponse(response);
    }

    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.toJson();
        outToClient.println(json);
        logger.info("Response sent: {}", json);
    }

    private void handleMailbox(String boxType) throws IOException {
        String response = null;
        List<Mail> mailsToRead = mailService.getMailsToRead(boxType);
        if(mailsToRead.isEmpty()){
            response = "Mailbox is empty";
        } else{
            for (Mail mail : mailsToRead) {
                response = "From: " + mail.getSender().getUsername() + "\n Message: " + mail.getMessage();
            }
            mailService.markMailsAsRead(boxType);
        }
        sendResponse(response);
    }

    private void handleEmpty(String boxType){
        sendResponse("Mails deleted successfully");
        mailService.emptyMailbox(boxType);
    }

    private void handleLogout() {
        userManager.logoutCurrentUser();
        logger.info("User successfully logged out.");
        sendResponse("Successfully logged out");

    }
}
