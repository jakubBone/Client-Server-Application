package request;

import lombok.Getter;
import lombok.Setter;
import user.credential.User;

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
    String userToUpdate;
    String newPassword;
    String userToSwitch;
    User.Role newRole;
}

