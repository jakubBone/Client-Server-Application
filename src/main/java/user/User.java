package user;

import mail.MailBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected String username;
    protected String hashedPassword;
    protected Role role;
    protected MailBox mailBox;

    public enum Role {
        ADMIN,
        USER;
    }

    public User(String username, String hashedPassword, Role role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.mailBox = new MailBox();
    }
}

