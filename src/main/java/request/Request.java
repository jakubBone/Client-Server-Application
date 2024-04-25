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
    String updateOperation;
    String userToUpdate;
    String newPassword;

    public Request(String requestCommand) {
        this.requestCommand = requestCommand;
    }

    public Request() {
    }
}

