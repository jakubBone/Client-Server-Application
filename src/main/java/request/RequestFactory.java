package request;

import client.ClientConnection;
import user.User;
import shared.UserInteraction;

import lombok.extern.log4j.Log4j2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class RequestFactory {

    private BufferedReader userInput;
    UserInteraction userInteraction;
    ClientConnection connection;

    public RequestFactory(ClientConnection clientConnection) {
        this.connection = clientConnection;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userInteraction = new UserInteraction(userInput);
        log.info("RequestFactory instance created");
    }

    public Request createRequest(String requestCommand) throws IOException {
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
                return new AuthRequest(requestCommand, username, password);
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new ServerDetailsRequest(requestCommand);
            case "LOGOUT":
                return new LogoutRequest(requestCommand);
        }
        log.warn("Unknown login menu request: {}", requestCommand);
        return null;
    }

    public Request getMailboxMenuRequest(String requestCommand) throws IOException {
        switch (requestCommand.toUpperCase()){
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return new WriteRequest(requestCommand, recipient, message);
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                return new MailBoxRequest(requestCommand, boxOperation, mailbox);
            case "UPDATE":
                return getAccountUpdateRequest();
            case "SWITCH":
                String userToSwitch = userInteraction.getUserToSwitch();
                return new AdminSwitchUserRequest(requestCommand, userToSwitch);
            case "LOGOUT":
                return new LogoutRequest(requestCommand);
        }
        log.warn("Unknown mailbox menu request: {}", requestCommand);
        return null;
    }

    public Request getAccountUpdateRequest() throws IOException {
        log.info("Creating account update request")
        if (connection.isAuthorized()) {
            log.info("Authorization succeeded");
            String updateOperation = userInteraction.chooseUpdateOperation();
            String userToUpdate = userInteraction.chooseUserToUpdate();
            switch (updateOperation) {
                case "PASSWORD":
                    String newPassword = userInteraction.getNewPassword();
                    return new AdminChangePasswordRequest(updateOperation, userToUpdate, newPassword);
                case "DELETE":
                    return new AdminDeleteUserRequest(updateOperation, userToUpdate);
                case "ROLE":
                    User.Role newRole = userInteraction.chooseRole();
                    return new AdminChangeRoleRequest(updateOperation, userToUpdate, newRole);
            }
            log.warn("Unknown update operation: {}", updateOperation);
            return null;
        } else {
            log.info("Authorization failed");
            return null;
        }
        return null;
    }
}