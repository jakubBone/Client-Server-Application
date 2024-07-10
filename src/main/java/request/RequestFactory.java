package request;

import lombok.extern.log4j.Log4j2;
import lombok.Getter;
import lombok.Setter;
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

import shared.UserInput;
import user.credential.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
@Getter
@Setter
public class RequestFactory {
    private BufferedReader reader;
    private UserInput userInput;
    private ClientConnection connection;

    public RequestFactory(ClientConnection clientConnection) {
        this.connection = clientConnection;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.userInput = new UserInput(reader);
    }

    public Request getRequest(String command) throws IOException {
        switch (command.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                return new AuthRequest(command, userInput.getUsername(), userInput.getPassword());
            case "LOGOUT":
                return new LogoutRequest(command);
            case "WRITE":
                return new MailWriteRequest(command, userInput.getRecipient(), userInput.getMessage());
            case "MAILBOX":
                return getMailboxRequest();
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new ServerDetailsRequest(command);
            case "SWITCH":
                return new UserSwitchRequest(command, userInput.getUserToSwitch());
            case "UPDATE":
                return getUpdateRequest();
            default:
                log.warn("Unknown operation: {}", command);
                return null;
        }
    }

    public Request getMailboxRequest() throws IOException {
        String boxOperation = userInput.chooseBoxOperation();
        String boxType = userInput.chooseBoxType();
        switch (boxOperation) {
            case "READ":
                return new MailsReadRequest(boxOperation, boxType);
            case "DELETE":
                return new MailsDeleteRequest(boxOperation, boxType);
            default:
                log.warn("Unknown mailbox operation: {}", boxOperation);
                return null;
        }
    }

    public Request getUpdateRequest() throws IOException {
            String update = userInput.chooseUpdateOperation();
            String userToUpdate = userInput.chooseUserToUpdate();
            switch (update) {
                case "PASSWORD":
                    String newPassword = userInput.getNewPassword();
                    return new UserChangePasswordRequest(update, userToUpdate, newPassword);
                case "REMOVE":
                    return new UserRemoveRequest(update, userToUpdate);
                case "ROLE":
                    User.Role newRole = userInput.chooseRole();
                    return  new UserChangeRoleRequest(update, userToUpdate, newRole);
                default:
                    log.warn("Unknown update operation: {}", update);
                    return null;
            }
        }
}