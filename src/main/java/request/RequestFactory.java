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
                return new LoginRegisterRequest(requestName, username, password);
            case "HELP":
                return new HelpRequest(requestName);
            case "WRITE":
                String recipient = userInteraction.getRecipient();
                String message = userInteraction.getMessage();
                return new WriteRequest(requestName, recipient, message);
            case "MAILBOX":
                String boxOperation = userInteraction.chooseBoxOperation();
                String mailbox = userInteraction.chooseMailBox();
                return new MailBoxRequest(requestName, boxOperation, mailbox);
            case "UPDATE":
                    return new UpdateRequest(requestName);
            case "LOGOUT":
                return new LogoutRequest(requestName);
            default:
                return null;
        }
    }

    public Request extendUpdateRequest(UserInteraction userInteraction) throws IOException{
        String updateOperation = userInteraction.chooseAccountUpdate();
        String userToUpdate = userInteraction.chooseUserToUpdate();
        String newPassword = null;
        if (updateOperation.equals("PASSWORD")) {
            newPassword = userInteraction.getNewPassword();
        }
        return new UpdateRequest(updateOperation, userToUpdate, newPassword);
    }

        /*requestType = new UpdateRequest(updateOperation, userToUpdate, newPassword);
        jsonRequest = gson.toJson(requestType);
        connection.sendRequest(jsonRequest);
        log.info("User updated {} for {}", updateOperation, userToUpdate);
        connection.readResponse();*/
}
