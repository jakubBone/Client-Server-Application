package request;

import lombok.Getter;
import lombok.Setter;

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


    public Request(String requestCommand) {
        this.requestCommand = requestCommand;
    }

    public Request() {
    }
}

