package request;

import client.ClientConnection;
import user.UserManager;
import utils.UserInteraction;

import lombok.extern.log4j.Log4j2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class RequestFactory {

    private BufferedReader userInput;
    UserInteraction userInteraction;
    UserManager userManager;
    ClientConnection connection;

    public RequestFactory(ClientConnection clientConnection) {
        this.connection = clientConnection;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userInteraction = new UserInteraction(userInput);
        this.userManager = new UserManager();
    }

    public Request createRequest(String request) throws IOException {
            if(!connection.isLoggedIn()){
                return getLoginMenuRequest(request);
            } else {
                return getMailboxMenuRequest(request);
            }
    }

    public Request getLoginMenuRequest(String request) throws IOException {
        switch (request.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                String username = userInteraction.getUsername();
                String password = userInteraction.getPassword();
                return new CredentialRequest(request, username, password);
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new ServerInfoRequest(request);
            case "LOGOUT":
                return new CredentialRequest(request);
            default:
                return null;
        }
    }

    public Request getMailboxMenuRequest(String request) throws IOException {
        switch (request.toUpperCase()){
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return new WriteRequest(request, recipient, message);
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                return new MailBoxRequest(request, boxOperation, mailbox);
            case "UPDATE":
                return getAccountUpdateRequest(request);
            case "LOGOUT":
                return new CredentialRequest(request);
            default:
                return null;
        }
    }

    public Request getAccountUpdateRequest(String request) throws IOException {
        if (connection.isAuthorized()) {
            log.info("Authorization success for admin");
            String updateOperation = userInteraction.chooseUpdateOperation();
            String userToUpdate = userInteraction.chooseUserToUpdate();
            switch (updateOperation) {
                case "PASSWORD":
                    String newPassword = userInteraction.getNewPassword();
                    return new AccountUpdateRequest(request, updateOperation, userToUpdate, newPassword);
                case "DELETE":
                    return new AccountUpdateRequest(request, updateOperation, userToUpdate);
                default:
                    return null;
            }
        } else {
            log.info("Authorization failed: no admin permission");
            return null;
        }
    }
}