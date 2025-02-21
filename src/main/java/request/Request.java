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
    private String newPassword;
    private String message;
    private String user;
    private User.Role newRole;
}