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
        request.requestCommand = requestCommand;
        request.username = username;
        request.password = password;
        log.info("AuthRequest created for user: {}", username);
        return request;
    }

    public Request createLogoutRequest(String requestCommand) {
        request.requestCommand = requestCommand;
        log.info("LogoutRequest created with command: {}", requestCommand);
        return request;
    }

    /**
     * Creates a server details request.
     * @param requestCommand The request command (e.g., "INFO", "UPTIME")
     */
    public Request createServerDetailsRequest(String requestCommand) {
        request.requestCommand = requestCommand;
        log.info("ServerDetailsRequest created with command: {}", requestCommand);
        return request;
    }

    /**
     * Creates a write (send mail) request.
     * @param requestCommand The request command ("WRITE")
     */
    public Request createWriteRequest(String requestCommand, String recipient, String message) {
        request.requestCommand = requestCommand;
        request.recipient = recipient;
        request.message = message;
        log.info("WriteRequest created for recipient: {}", recipient);
        return request;
    }

    /**
     * Creates a mailbox operation request.
     * @param requestCommand The request command ("MAILBOX")
     * @param boxOperation The mailbox operation (e.g., "READ", "DELETE")
     * @param mailbox The mailbox type (e.g., "UNREAD", "SENT")
     */
    public Request createMailBoxRequest(String requestCommand, String boxOperation, String mailbox) {
        request.requestCommand = requestCommand;
        request.boxOperation = boxOperation;
        request.boxType = mailbox;
        log.info("MailBoxRequest created with operation: {} for mailbox: {}", boxOperation, mailbox);
        return request;
    }

    /**
     * Creates an admin password change request.
     * @param updateOperation The update operation ("PASSWORD")
     */
    public Request createAdminChangePasswordRequest(String updateOperation, String userToUpdate, String newPassword) {
        request.requestCommand = updateOperation;
        request.userToUpdate = userToUpdate;
        request.newPassword = newPassword;
        log.info("AdminChangePasswordRequest created for user: {}", userToUpdate);
        return request;
    }


    public Request createAdminDeleteUserRequest(String updateOperation, String userToDelete) {
        request.requestCommand = updateOperation;
        request.userToUpdate = userToDelete;
        log.info("AdminDeleteUserRequest created for user: {}", userToDelete);
        return request;
    }

    public Request createAdminChangeRoleRequest(String updateOperation, String userToUpdate, User.Role role) {
        request.requestCommand = updateOperation;
        request.userToUpdate = userToUpdate;
        request.newRole = role;
        log.info("AdminChangeRoleRequest created for user: {} with new role: {}", userToUpdate, role);
        return request;
    }

    /**
     * Creates an admin switch user request.
     * @param requestCommand The request command ("SWITCH")
     * @param username The username of the user to switch to
     */
    public Request createAdminSwitchUserRequest(String requestCommand, String username) {
        request.requestCommand = requestCommand;
        request.userToSwitch = username;
        log.info("AdminSwitchUserRequest created for user: {}", username);
        return request;
    }
}
