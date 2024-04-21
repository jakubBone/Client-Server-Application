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
        if(!userManager.isAdmin()){
            logger.warn("Unauthorized attempt to update by non-admin user.");
            outToClient.println("Operation failed: Not authorized\n<<END>>");
        } else {
            logger.info("Authorized update attempt by admin user.");
            isAuthorized = true;
            outToClient.println("Operation succeeded: Authorized\n<<END>>");
            String updateRequest = inFromClient.readLine();
            String[] updateOperationParts = updateRequest.split(" ", 3);
            handleUpdate(updateOperationParts[0], updateOperationParts[1], updateOperationParts[2]);
        }
    }

    private void handleUpdate(String updateOperation, String userToUpdate, String newPassword) throws IOException {
        User searchedUser = userManager.findUserOnTheList(userToUpdate);
        Admin admin = new Admin();
        if(!(searchedUser == null)) {
            switch (updateOperation.toUpperCase()) {
                case "PASSWORD":
                    admin.changePassword(searchedUser, newPassword);
                    outToClient.println(searchedUser.getUsername() + " password change successful\n<<END>>");
                    logger.info("Password changed successfully for user: {}", searchedUser.getUsername());
                    break;
                case "DELETE":
                    if(searchedUser.getRole().equals(User.Role.ADMIN)){
                        outToClient.println("Operation failed: admin account cannot be deleted\n<<END>>");
                        logger.warn("Attempted to impossible delete admin account for user: {}", searchedUser.getUsername());
                    } else {
                        admin.deleteUser(searchedUser);
                        outToClient.println(searchedUser.getUsername() + " account deletion successful\n<<END>>");
                        logger.info("User account deleted successfully: {}", searchedUser.getUsername());
                    }
                    break;
            }
        } else {
            outToClient.println("Update failed: " + userToUpdate + " not found\n<<END>>");
            logger.warn("Failed to find user for update: {}", userToUpdate);
        }
    }


    /*public void handleAuthentication(String command, String username, String password) throws IOException {
        UserSerializer serializer = new UserSerializer();
        String out = null;
        switch (command) {
            case "REGISTER":
                userManager.register(username, password);
                logger.info("Registration attempted for user: {}", username);
                out = "Registration successful";
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    logger.info("User logged in successfully: {}", username);
                    UserManager.currentLoggedInUser = user;
                    out = "Login successful";
                } else {
                    out = "Login failed: Incorrect username or password";
                    logger.warn("Login attempt failed for user: {}", username);
                }
                break;
        }
        System.out.println(serializer.toJson(out));
        outToClient.println(serializer.toJson(out));
    }*/
    /*public void handleAuthentication(String command, String username, String password) throws IOException {
        switch (command) {
            case "REGISTER":
                userManager.register(username, password);
                logger.info("Registration attempted for user: {}", username);
                outToClient.println("Registration successful\n<<END>>");
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    logger.info("User logged in successfully: {}", username);
                    UserManager.currentLoggedInUser = user;
                    outToClient.println("Login successful\n<<END>>");
                } else {
                    logger.warn("Login attempt failed for user: {}", username);
                    outToClient.println("Login failed: Incorrect username or password\n<<END>>");
                }
                break;
        }
    }*/
    public void handleAuthentication(String command, String username, String password) throws IOException {
        JsonConverter jsonResponse;
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
                    outToClient.println("Login successful\n<<END>>");
                } else {
                    logger.warn("Login attempt failed for user: {}", username);
                    outToClient.println("Login failed: Incorrect username or password\n<<END>>");
                }
                break;
        }
        jsonResponse = new JsonConverter(response);
        String json = jsonResponse.toJson();
        outToClient.println(json);
    }

    public void handleHelpRequest(String request) {
        String json = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        switch (request) {
            case "UPTIME":
                json = gson.toJson(serverInfo.getUptime());

                break;
            case "INFO":
                json = gson.toJson(serverInfo.getServerDetails());

                break;
            case "HELP":
                json = gson.toJson(serverInfo.getCommands());

                break;
            default:
                json = gson.toJson(serverInfo.getInvalidMessage());

        }
        json += "\n<<END>>\n";
        outToClient.println(json);
    }
    /*public void handleHelpRequest(String request, PrintWriter outToClient) {
        String 
        switch (request) {
            case "UPTIME":
                outToClient.println(serverInfo.getUptime()+ "<<END>>");
                logger.info("Time from server setup: " + serverInfo.getUptime());
                break;
            case "INFO":
                outToClient.println(serverInfo.getServerDetails());
                logger.info("Server version: " + serverInfo.getVersion() + " / Setup date: " + serverInfo.getServerTimeCreation());
                break;
            case "HELP":
                outToClient.println(serverInfo.getCommands());
                logger.info("Command list displayed");
                break;
            default:
                outToClient.println(serverInfo.getInvalidMessage());
                logger.warn("Invalid input ---------");
        }
        json += "\n<<END>>\n";
        outToClient.println("<<END>>");
    }*/

    private void handleWrite(String recipient, String message) throws IOException {
        User recipientUser = userManager.getRecipientByUsername(recipient);
        if (recipientUser != null) {
            if(recipientUser.getMailBox().ifBoxFull()){
                logger.warn("Mail sending failed, recipient's mailbox is full: {}", recipient);
                outToClient.println("Sending failed: Recipient's mailbox is full\n<<END>>");
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    logger.info("Mail sent successfully to: {}", recipient);
                    outToClient.println("Mail sent successfully\n<<END>>");
                } else {
                    logger.warn("Mail sending failed, message too long for recipient: {}", recipient);
                    outToClient.println("Sending failed: Message too long (maximum 255 characters)\n<<END>>");
                }
            }
        } else {
            logger.warn("Mail sending failed, recipient not found: {}", recipient);
            outToClient.println("Sending failed: Recipient not found\n<<END>>");
        }
    }
    private void handleMailbox(String boxType) throws IOException {
        List<Mail> mailsToRead = mailService.getMailsToRead(boxType);
        if(mailsToRead.isEmpty()){
            outToClient.println("Mailbox is empty");
        } else{
            for (Mail mail : mailsToRead) {
                outToClient.println("From: " + mail.getSender().getUsername() + "\n Message: " + mail.getMessage());
            }
            mailService.markMailsAsRead(boxType);
        }
        outToClient.println("<<END>>");
    }

    private void handleEmpty(String boxType){
        mailService.emptyMailbox(boxType);
        outToClient.println("Mails deleted successfully\n<<END>>");
    }

    private void handleLogout() {
        userManager.logoutCurrentUser();
        logger.info("User successfully logged out.");
        outToClient.println("Successfully logged out\n<<END>>");
    }
}
