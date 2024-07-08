package request;

import lombok.Getter;
import lombok.Setter;
import user.credential.User;

@Getter
@Setter
public class Request {
    private String command;
    private String username;
    private String password;
    private String recipient;
    private String message;
    private String mailboxOperation;
    private String boxType;
    private String userToUpdate;
    private String newPassword;
    private String userToSwitch;
    private User.Role newRole;
}