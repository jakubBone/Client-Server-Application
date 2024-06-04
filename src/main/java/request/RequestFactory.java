package request;

import utils.UserInteraction;

import java.io.IOException;

public class RequestFactory {

    public Request createRequest(String requestName, UserInteraction userInteraction) throws IOException {
        switch (requestName.toUpperCase()) {
            case "REGISTER":
            case "LOGIN":
                String username = userInteraction.getUsername();
                String password = userInteraction.getPassword();
                return new CredentialRequest(requestName, username, password);
            case "HELP":
            case "UPTIME":
            case "INFO":
                return new ServerInfoRequest(requestName);
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return new WriteRequest(requestName, recipient, message);
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                return new MailBoxRequest(requestName, boxOperation, mailbox);
            case "UPDATE":
                    return new AccountUpdateRequest(requestName);
            case "LOGOUT":
                return new CredentialRequest(requestName);
            default:
                return null;
        }
    }

    public Request createAccountUpdateRequest(UserInteraction userInteraction) throws IOException{
        String updateOperation = userInteraction.chooseAccountUpdateOperation();
        String userToUpdate = userInteraction.chooseUserToUpdate();
        String newPassword = null;
        if (updateOperation.equals("PASSWORD")) {
            newPassword = userInteraction.getNewPassword();
        }

        return new AccountUpdateRequest(updateOperation, userToUpdate, newPassword);
    }
}
