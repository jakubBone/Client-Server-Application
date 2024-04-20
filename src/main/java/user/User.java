package user;

import mail.MailBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected static final Logger logger = LogManager.getLogger(User.class);
    protected String username;
    protected String password;
    protected Role role;
    protected MailBox mailBox;

    public enum Role {
        ADMIN,
        USER;
    }

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