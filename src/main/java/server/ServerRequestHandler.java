package server;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;
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

@Log4j2
public class ServerRequestHandler {
    private final PrintWriter outToClient;
    private final BufferedReader inFromClient;
    private final UserManager userManager;
    private final MailService mailService;
    private final ServerInfo serverInfo;
    private JsonConverter jsonResponse;
    private boolean isAuthorized;
    

    public ServerRequestHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.mailService = new MailService();
        this.serverInfo = new ServerInfo();
        isAuthorized = false;
    }

    public void handleClientRequest() {
        String request = null;
        try {
            while ((request = inFromClient.readLine()) != null) {
                String[] requestParts = request.split(" ", 3); // The array storing main request details
                String command = requestParts[0].toUpperCase();
                log.info("Handling command: {}", command);
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
                        handleMailbox(requestParts[1], requestParts[2]);
                        break;
                    case "UPDATE":
                        handleUpdateRequest();
                        break;
                    case "LOGOUT":
                        handleLogout();
                        break;
                }
                log.info("Completed authentication command: {}", command);
            }
        } catch (IOException ex) {
            log.error("IOException occurred while processing the request: {}. Error: ", request, ex);
        }
    }


    // Handles UPDATE request for updating user data. It checks for authorization before proceeding
    private void handleUpdateRequest() throws IOException{
        if(userManager.isAdmin()){
            log.info("Authorized update attempt by admin user");
            isAuthorized = true;
            sendResponse("Operation succeeded: Authorized");
            String updateRequest = inFromClient.readLine();
            String[] updateOperationParts = updateRequest.split(" ", 3);
            handleUpdate(updateOperationParts[0], updateOperationParts[1], updateOperationParts[2]);
        } else {
            log.warn("Unauthorized attempt to update by non-admin user.");
            sendResponse("Operation failed: Not authorized");
        }
    }

    // Handles a specific update operation, such as changing a password or deleting a user
    private void handleUpdate(String updateOperation, String username, String newPassword) throws IOException {
        User userToUpdate = userManager.findUserByUsername(username);
        String response = null;
        if(userToUpdate != null ) {
            switch (updateOperation.toUpperCase()) {
                case "PASSWORD":
                    userManager.changePassword(userToUpdate, newPassword);
                    response = userToUpdate.getUsername() + " password change successful";
                    log.info("Password changed successfully for user: {}", userToUpdate.getUsername());
                    break;
                case "DELETE":
                    if(userToUpdate.getRole().equals(User.Role.ADMIN)){
                        response = "Operation failed: admin account cannot be deleted";
                        log.warn("Attempted to impossible delete admin account for user: {}", userToUpdate.getUsername());
                    } else {
                        userManager.deleteUser(userToUpdate);
                        response = userToUpdate.getUsername() + " account deletion successful";
                        log.info("User account deleted successfully: {}", userToUpdate.getUsername());
                    }
                    break;
            }
        } else {
            response = "Update failed: " + username + " not found";
            log.warn("Failed to find user for update: {}", username);
        }
        sendResponse(response);
    }

    public void handleAuthentication(String command, String username, String password) throws IOException {
        String response = null;
        switch (command) {
            case "REGISTER":
                log.info("Registration attempted for user: {}", username);
                String registerStatus = userManager.register(username, password);
                if(registerStatus.equals("User exist")){
                    response = "Login failed: Existing user";
                } else {
                    response = "Registration successful";
                }
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    log.info("User logged in successfully: {}", username);
                    UserManager.currentLoggedInUser = user;
                    response = "Login successful";
                } else {
                    log.warn("Login attempt failed for user: {}", username);
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
        log.info("Received help request: {}", request);
        String response = null;
        switch (request) {
            case "UPTIME":
                response = serverInfo.getUptime().toString();
                log.info("UPTIME requested, response: {}", response);
                break;
            case "INFO":
                response = serverInfo.getServerDetails().toString();
                log.info("INFO requested, response: {}", response);
                break;
            case "HELP":
                response = serverInfo.getCommands().toString();
                log.info("HELP requested, response: {}", response);
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
                log.warn("Mail sending failed, recipient's mailbox is full: {}", recipient);
                response = "Sending failed: Recipient's mailbox is full";
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    log.info("Mail sent successfully to: {}", recipient);
                    response = "Mail sent successfully";
                } else {
                    log.warn("Mail sending failed, message too long for recipient: {}", recipient);
                    response = "Sending failed: Message too long (maximum 255 characters)";
                }
            }
        } else {
            log.warn("Mail sending failed, recipient not found: {}", recipient);
            response = "Sending failed: Recipient not found";
        }
        sendResponse(response);
    }

    public void sendResponse(String response){
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.toJson();
        outToClient.println(json);
        log.info("Response sent: {}", json);
    }

    private void handleMailbox(String mailOperation, String boxType) throws IOException {
        if(mailOperation.equals("READ")){
            handleRead(boxType);
        } else if(mailOperation.equals("EMPTY")){
            handleEmpty(boxType);
        }
    }
    private void handleRead(String boxType){
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
        mailService.emptyMailbox(boxType);
        sendResponse("Mails deleted successfully");
    }

    private void handleLogout() {
        userManager.logoutCurrentUser();
        sendResponse("Successfully logged out");
    }
}
