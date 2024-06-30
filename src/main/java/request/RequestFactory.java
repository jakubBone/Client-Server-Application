package request;

import lombok.extern.log4j.Log4j2;
import user.credential.User;

/**
 * The RequestFactory class creates different types of Request objects based on the command.
 * It includes methods for creating authentication, server details, mailbox, and account update requests.
 */

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
        log.info("AuthRequest created for user: {}", username);
        return request;
    }

    public Request createLogoutRequest(String requestCommand) {
        request.setRequestCommand(requestCommand);
        log.info("LogoutRequest created with command: {}", requestCommand);
        return request;
    }

    /**
     * Creates a server details request.
     * @param requestCommand The request command (e.g., "INFO", "UPTIME")
     */
    public Request createServerDetailsRequest(String requestCommand) {
        request.setRequestCommand(requestCommand);
        log.info("ServerDetailsRequest created with command: {}", requestCommand);
        return request;
    }

    /**
     * Creates a write (send mail) request.
     * @param requestCommand The request command ("WRITE")
     */
    public Request createWriteRequest(String requestCommand, String recipient, String message) {
        request.setRequestCommand(requestCommand);
        request.setRecipient(recipient);
        request.setMessage(message);
        log.info("WriteRequest created for recipient: {}", recipient);
        return request;
    }

    /**
     * Creates a boxType operation request.
     * @param requestCommand The request command ("MAILBOX")
     * @param boxOperation The boxType operation (e.g., "READ", "DELETE")
     * @param boxType The boxType type (e.g., "UNREAD", "SENT")
     */
    public Request createMailBoxRequest(String requestCommand, String boxOperation, String boxType) {
        request.setRequestCommand(requestCommand);
        request.setBoxOperation(boxOperation);
        request.setBoxType(boxType);
        log.info("MailBoxRequest created with operation: {} for boxType: {}", boxOperation, boxType);
        return request;
    }

    /**
     * Creates an admin password change request.
     * @param updateOperation The update operation ("PASSWORD")
     */
    public Request createAdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToUpdate);
        request.setNewPassword(newPassword);
        log.info("AdminChangePasswordRequest created for user: {}", userToUpdate);
        return request;
    }


    public Request createAdminDeleteUserRequest(String updateOperation, String userToDelete) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToDelete);
        log.info("AdminDeleteUserRequest created for user: {}", userToDelete);
        return request;
    }

    public Request createAdminChangeRoleRequest(String updateOperation, String userToUpdate, User.Role role) {
        request.setRequestCommand(updateOperation);
        request.setUserToUpdate(userToUpdate);
        request.setNewRole(role);
        log.info("AdminChangeRoleRequest created for user: {} with new role: {}", userToUpdate, role);
        return request;
    }

    /**
     * Creates an admin switch user request.
     * @param requestCommand The request command ("SWITCH")
     */
    public Request createAdminSwitchUserRequest(String requestCommand, String userToSwitch) {
        request.setRequestCommand(requestCommand);
        request.setUserToSwitch(userToSwitch);
        log.info("AdminSwitchUserRequest created for user: {}", userToSwitch);
        return request;
    }
}
