package request;

import lombok.extern.log4j.Log4j2;
import user.credential.User;

@Log4j2
public class RequestFactory {
    private Request request;

    public RequestFactory() {
        this.request = new Request();
    }

    public Request createAuthRequest(String requestCommand, String username, String password) {
        request.setRequestCommand(requestCommand);
        request.setUsername(username);
        request.setPassword(password);
        log.info("Authorization request created for user: {}", username);
        return request;
    }

    public Request createLogoutRequest(String requestCommand) {
        request.setRequestCommand(requestCommand);
        log.info("Logout request created with command: {}", requestCommand);
        return request;
    }

    public Request createServerDetailsRequest(String requestCommand) {
        request.setRequestCommand(requestCommand);
        log.info("Server details request created with command: {}", requestCommand);
        return request;
    }

    public Request createWriteRequest(String requestCommand, String recipient, String message) {
        request.setRequestCommand(requestCommand);
        request.setRecipient(recipient);
        request.setMessage(message);
        log.info("Write mails request created for recipient: {}", recipient);
        return request;
    }

    public Request createReadMailsRequest(String boxOperation, String boxType) {
        request.setRequestCommand(boxOperation);
        request.setBoxType(boxType);
        log.info("Read mails request created with operation: {} for boxType: {}", boxOperation, boxType);
        return request;
    }

    public Request createDeleteMailsRequest(String boxOperation, String boxType) {
        request.setRequestCommand(boxOperation);
        request.setBoxType(boxType);
        log.info("Delete mails request created with operation: {} for boxType: {}", boxOperation, boxType);
        return request;
    }

    public Request createAdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToUpdate);
        request.setNewPassword(newPassword);
        log.info("Password change request created for user: {}", userToUpdate);
        return request;
    }


    public Request createUserRemoveRequest(String updateOperation, String userToDelete) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToDelete);
        log.info("Remove request created for user: {}", userToDelete);
        return request;
    }

    public Request createUserRoleChangeRequest(String updateOperation, String userToUpdate, User.Role role) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToUpdate);
        request.setNewRole(role);
        log.info("Role change request created for user: {} with new role: {}", userToUpdate, role);
        return request;
    }

    public Request createUserSwitchRequest(String requestCommand, String userToSwitch) {
        request.setRequestCommand(requestCommand);
        request.setUserToSwitch(userToSwitch);
        log.info("Switch user request created for user: {}", userToSwitch);
        return request;
    }
}
