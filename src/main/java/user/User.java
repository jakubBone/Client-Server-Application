package user;

import mail.MailBox;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    public enum Role {
        ADMIN,
        USER;
    }
    protected String username;
    protected String password;
    protected Role role;
    protected MailBox mailBox;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mailBox = new MailBox();
    }

    public String toString() {
        return username;
    }
}