package request;

import lombok.extern.log4j.Log4j2;
import client.ClientConnection;
import request.auth.AuthRequest;
import request.auth.LogoutRequest;
import request.mail.MailWriteRequest;
import request.mail.MailsDeleteRequest;
import request.mail.MailsReadRequest;
import request.mail.ServerDetailsRequest;
import request.user.UserChangePasswordRequest;
import request.user.UserChangeRoleRequest;
import request.user.UserRemoveRequest;
import request.user.UserSwitchRequest;

import shared.UserInteraction;
import user.credential.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class RequestFactory {
    private BufferedReader userInput;
    private UserInteraction userInteraction;
    private ClientConnection connection;

    public RequestFactory(ClientConnection clientConnection) {
        this.connection = clientConnection;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userInteraction = new UserInteraction(userInput);
        log.info("RequestFactory instance created");
    }

    public Request getRequest(String command) throws IOException {
        switch (command.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                String username = userInteraction.getUsername();
                String password = userInteraction.getPassword();
                return new AuthRequest(command, username, password);
            case "LOGOUT":
                return new LogoutRequest(command);
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return new MailWriteRequest(command, recipient, message);
            case "MAILBOX":
                return getMailboxRequest();
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new ServerDetailsRequest(command);
            case "SWITCH":
                String userToSwitch = userInteraction.getUserToSwitch();
                return new UserSwitchRequest(command, userToSwitch);
            case "UPDATE":
                return getUpdateRequest();
            default:
                throw new IllegalArgumentException("Unknown command type: " + command);
        }
    }

    public Request getMailboxRequest() throws IOException {
        String boxOperation = userInteraction.chooseBoxOperation();
        String boxType = userInteraction.chooseBoxType();
        switch (boxOperation) {
            case "READ":
                return new MailsReadRequest(boxOperation, boxType);
            case "DELETE":
                return new MailsDeleteRequest(boxOperation, boxType);
            default:
                log.warn("Unknown update operation: {}", boxOperation);
                return null;
        }
    }

    public Request getUpdateRequest() throws IOException {
        if (connection.isUserAuthorized()) {
            log.info("Authorization succeeded");
            String update = userInteraction.chooseUpdateOperation();
            String userToUpdate = userInteraction.chooseUserToUpdate();
            switch (update) {
                case "PASSWORD":
                    String newPassword = userInteraction.getNewPassword();
                    return new UserChangePasswordRequest(update, userToUpdate, newPassword);
                case "REMOVE":
                    return new UserRemoveRequest(update, userToUpdate);
                case "ROLE":
                    User.Role newRole = userInteraction.chooseRole();
                    return  new UserChangeRoleRequest(update, userToUpdate, newRole);
                default:
                    log.warn("Unknown update operation: {}", update);
            }
        }
        log.warn("Authorization failed");
        return null;
    }
}