package server;

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

 /*
  * The ServerLogicHandler class is responsible for handling various client requests and processing server-side logic
  * It manages user authentication, mail operations, and server information
  */

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
                String[] requestParts = request.split(" ", 3); // The array storing main request details
                String command = requestParts[0].toUpperCase();
                logger.info("Handling command: {}", command);
                switch (command) {
                    case "REGISTER":
                    case "LOGIN":
                        handleAuthentication(command, requestParts[1], requestParts[2]);
                        break;
                    case "HELP":
                        handleHelpRequest(command);
                        break;
                    case "WRITE":
                        handleWrite(requestParts[1], requestParts[2]);
                        break;
                    case "MAILBOX":
                        if(requestParts[1].equals("READ")){
                            handleMailbox(requestParts[2]);
                        } else {
                            handleEmpty(requestParts[2]);
                        }
                        break;
                    case "UPDATE":
                        handleUpdateRequest();
                        break;
                    case "LOGOUT":
                        handleLogout();
                        break;
                }
                logger.info("Completed authentication command: {}", command);
            }
        } catch (IOException ex) {
            logger.error("IOException occurred while processing the request: {}. Error: {}", request, ex.getMessage());
        }
    }

    // Handles UPDATE request for updating user data. It checks for authorization before proceeding
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

    // Handles a specific update operation, such as changing a password or deleting a user
    private void handleUpdate(String updateOperation, String username, String newPassword) throws IOException {
        User userToUpdate = userManager.findUserByUsername(username);
        Admin admin = new Admin();
        String response = null;
        if(!(userToUpdate == null)) {
            switch (updateOperation.toUpperCase()) {
                case "PASSWORD":
                    admin.changePassword(userToUpdate, newPassword);
                    response = userToUpdate.getUsername() + " password change successful";
                    logger.info("Password changed successfully for user: {}", userToUpdate.getUsername());
                    break;
                case "DELETE":
                    if(userToUpdate.getRole().equals(User.Role.ADMIN)){
                        response = "Operation failed: admin account cannot be deleted";
                        logger.warn("Attempted to impossible delete admin account for user: {}", userToUpdate.getUsername());
                    } else {
                        admin.deleteUser(userToUpdate);
                        response = userToUpdate.getUsername() + " account deletion successful";
                        logger.info("User account deleted successfully: {}", userToUpdate.getUsername());
                    }
                    break;
            }
        } else {
            response = "Update failed: " + username + " not found";
            logger.warn("Failed to find user for update: {}", username);
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

    /*
     * Handles client requests for server information or help.
     * Needs to be improved
     */
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

    /*
     * Handles writing a mail to a specific recipient
     * Checks if the recipient exists and if the message length is within limits.
     */
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

    // Sends a response to the client after converting it to JSON format.
    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.toJson();
        outToClient.println(json);
        logger.info("Response sent: {}", json);
    }


    // Handles the reading of a specific mailbox type and sends the corresponding mails to the client
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
