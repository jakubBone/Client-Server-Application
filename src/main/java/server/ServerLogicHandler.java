package server;

import mail.Mail;
import mail.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.Admin;
import user.User;
import user.UserManager;
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
    private boolean isAuthorized = false;

    //private ServerInfoService helperService = new ServerInfoService();

    public ServerLogicHandler(PrintWriter outToClient, BufferedReader inFromClient) {
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.userManager = new UserManager();
        this.mailService = new MailService();
    }

    public void handleClientRequest() {
        logger.info("Starting to handle client requests.");
        String request = null;
        try {
            while ((request = inFromClient.readLine()) != null) {
                String[] parts = request.split(" ", 3);
                String command = parts[0].toUpperCase();
                logger.debug("Handling command: {}", command);
                switch (command) {
                    case "REGISTER":
                    case "LOGIN":
                        handleAuthentication(command, parts[1], parts[2]);
                        break;
                    case "HELP":
                        //helperService.handleHelpRequest(command, outToClient);
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
                logger.debug("Completed authentication command: {}", command);
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
            logger.warn("Authorized attempt to update by admin user.");
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
                    break;
                case "DELETE":
                    if(searchedUser.getRole().equals(User.Role.ADMIN)){
                        outToClient.println("Operation failed: admin account cannot be deleted\n<<END>>");
                    } else {
                        admin.deleteUser(searchedUser);
                        outToClient.println(searchedUser.getUsername() + " account deletion successful\n<<END>>");
                    }
                    break;
            }
        } else {
            outToClient.println("Update failed: " + userToUpdate + " not found\n<<END>>");
        }
    }


    public void handleAuthentication(String command, String username, String password) throws IOException {
        switch (command) {
            case "REGISTER":
                userManager.register(username, password);
                outToClient.println("Registration successful\n<<END>>");
                break;
            case "LOGIN":
                User user = userManager.login(username, password);
                if (user != null) {
                    logger.info("User logged in successfully: {}", username);
                    UserManager.currentLoggedInUser = user;
                    outToClient.println("Login successful\n<<END>>");
                } else {
                    outToClient.println("Login failed: Incorrect username or password\n<<END>>");
                }
                break;
        }
    }

    private void handleWrite(String recipient, String message) throws IOException {
        User recipientUser = userManager.getRecipientByUsername(recipient);
        if (recipientUser != null) {
            if(recipientUser.getMailBox().ifBoxFull()){
                outToClient.println("Sending failed: Recipient's mailbox is full\n<<END>>");
            } else {
                if(message.length() <= 255){
                    mailService.sendMail(new Mail(UserManager.currentLoggedInUser, recipientUser, message));
                    outToClient.println("Mail sent successfully\n<<END>>");
                } else {
                    outToClient.println("Sending failed: Message too long (maximum 255 characters)\n<<END>>");
                }
            }
        } else {
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
        outToClient.println("Successfully logged out\n<<END>>");
    }
}
