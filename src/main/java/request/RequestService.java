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

    public Request getRequest(String requestCommand) throws IOException {
        log.info("Creating request for command: {}", requestCommand);
        if(!connection.isLoggedIn()){
            return handleLoginMenuRequest(requestCommand);
        } else {
            return handleMailboxMenuRequest(requestCommand);
        }
    }

    public Request handleLoginMenuRequest(String requestCommand) throws IOException {
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

    public Request handleMailboxMenuRequest(String requestCommand) throws IOException {
        switch (requestCommand.toUpperCase()){
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return factory.createWriteRequest(requestCommand, recipient, message);
            case "MAILBOX":
                return handleMailboxRequest();
            case "UPDATE":
                return handleAccountUpdateRequest();
            case "SWITCH":
                String userToSwitch = userInteraction.getUserToSwitch();
                return factory.createUserSwitchRequest(requestCommand, userToSwitch);
            case "LOGOUT":
                return factory.createLogoutRequest(requestCommand);
            default:
                log.warn("Unknown boxType menu request: {}", requestCommand);
                return null;
        }
    }

    public Request handleMailboxRequest() throws IOException {
        String boxOperation = userInteraction.chooseBoxOperation();
        String boxType = userInteraction.chooseBoxType();
            switch (boxOperation.toUpperCase()) {
                case "READ":
                    return factory.createReadMailsRequest(boxOperation, boxType);
                case "DELETE":
                    return factory.createDeleteMailsRequest(boxOperation, boxType);
                default:
                    log.warn("Unknown update operation: {}", boxOperation);
                    return null;
            }
    }

    public Request handleAccountUpdateRequest() throws IOException {
        log.info("Creating account update request");

        if (connection.isUserAuthorized()) {
            log.info("Authorization succeeded");
            String updateOperation = userInteraction.chooseUpdateOperation();
            String userToUpdate = userInteraction.chooseUserToUpdate();
            switch (updateOperation.toUpperCase()) {
                case "PASSWORD":
                    String newPassword = userInteraction.getNewPassword();
                    return factory.createAdminChangePasswordRequest(updateOperation, userToUpdate, newPassword);
                case "REMOVE":
                    return factory.createUserRemoveRequest(updateOperation, userToUpdate);
                case "ROLE":
                    User.Role newRole = userInteraction.chooseRole();
                    return factory.createUserRoleChangeRequest(updateOperation, userToUpdate, newRole);
                default:
                    log.warn("Unknown update operation: {}", updateOperation);
            }
        }
        log.warn("Authorization failed");
        return null;
    }
}