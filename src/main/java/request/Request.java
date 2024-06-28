package request;

import lombok.Getter;
import lombok.Setter;
import user.User;

/**
 * The Request class represents a request sent from the client to the server.
 * It contains fields depending on the type of operation (e.g., login, sending a message, account update).
 */
@Getter
@Setter
public class Request {
    String requestCommand;
    String username;
    String password;
    String recipient;
    String message;
    String boxOperation;
    String boxType;
    String updateOperation;
    String userToUpdate;
    String newPassword;
    String userToSwitch;
    User.Role newRole;
}

