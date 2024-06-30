package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ClientConnection;
import lombok.extern.log4j.Log4j2;
import shared.UserInteraction;
import user.credential.User;

@Log4j2
public class RequestService {

    private BufferedReader userInput;
    private UserInteraction userInteraction;
    private ClientConnection connection;
    private RequestFactory factory;

    public RequestService(ClientConnection clientConnection) {
        this.connection = clientConnection;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userInteraction = new UserInteraction(userInput);
        this.factory = new RequestFactory();
        log.info("RequestFactory instance created");
    }

    /**
     * Creates a request based on the current connection state (logged in or not).
     * Delegates to the appropriate menu request handler.
     */
    public Request getRequest(String requestCommand) throws IOException {
        log.info("Creating request for command: {}", requestCommand);

        if(!connection.isLoggedIn()){
            return getLoginMenuRequest(requestCommand);
        } else {
            return getMailboxMenuRequest(requestCommand);
        }
    }

    public Request getLoginMenuRequest(String requestCommand) throws IOException {
        switch (requestCommand.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                String username = userInteraction.getUsername();
                String password = userInteraction.getPassword();
                return factory.createAuthRequest(requestCommand, username, password);
            case "HELP":
            case "INFO":
            case "UPTIME":
                return factory.createServerDetailsRequest(requestCommand);
            case "LOGOUT":
                return factory.createLogoutRequest(requestCommand);
            default:
                log.warn("Unknown login menu request: {}", requestCommand);
                return null;
        }
    }

    public Request getMailboxMenuRequest(String requestCommand) throws IOException {
        switch (requestCommand.toUpperCase()){
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return factory.createWriteRequest(requestCommand, recipient, message);
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                return factory.createMailBoxRequest(requestCommand, boxOperation, mailbox);
            case "UPDATE":
                return getAccountUpdateRequest();
            case "SWITCH":
                String userToSwitch = userInteraction.getUserToSwitch();
                return factory.createAdminSwitchUserRequest(requestCommand, userToSwitch);
            case "LOGOUT":
                return factory.createLogoutRequest(requestCommand);
            default:
                log.warn("Unknown mailbox menu request: {}", requestCommand);
                return null;
        }
    }

    /**
     * Ensures the current user is authorized to perform the update.
     */
    public Request getAccountUpdateRequest() throws IOException {
        log.info("Creating account update request");

        if (connection.isAuthorized()) {
            log.info("Authorization succeeded");
            String updateOperation = userInteraction.chooseUpdateOperation();
            String userToUpdate = userInteraction.chooseUserToUpdate();
            switch (updateOperation) {
                case "PASSWORD":
                    String newPassword = userInteraction.getNewPassword();
                    return factory.createAdminChangePasswordRequest(updateOperation, userToUpdate, newPassword);
                case "DELETE":
                    return factory.createAdminDeleteUserRequest(updateOperation, userToUpdate);
                case "ROLE":
                    User.Role newRole = userInteraction.chooseRole();
                    return factory.createAdminChangeRoleRequest(updateOperation, userToUpdate, newRole);
                default:
                    log.warn("Unknown update operation: {}", updateOperation);
                    return null;
            }
        }
        log.warn("Authorization failed");
        return null;
    }
}