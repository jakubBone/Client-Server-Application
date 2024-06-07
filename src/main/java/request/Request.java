package request;

import lombok.Getter;
import lombok.Setter;
import user.User;

@Getter
@Setter
public class Request {
    String requestCommand;
    String username;
    String password;
    String recipient;
    String message;
    String boxOperation;
    String mailbox;
    String updateOperation;
    String userToUpdate;
    String newPassword;
    String userToSwitch;
    User.Role newRole;

    public Request(String requestCommand) {
        this.requestCommand = requestCommand;
    }

    public Request() {
    }
}

