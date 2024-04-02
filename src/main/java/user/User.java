package user;

import exceptions.UserAuthenticationException;
import mail.MailBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {
    protected static final Logger logger = LogManager.getLogger(User.class);
    protected String username;
    protected String password;
    protected int hashedPassword;
    protected Role role;
    protected MailBox mailBox;
    protected boolean isUserLoggedIn;

    public enum Role {
        ADMIN,
        USER;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mailBox = new MailBox();
        this.isUserLoggedIn = false;
        this.hashedPassword = password.hashCode();
    }

    public void requestPasswordChange(List <String> passwordChangeRequests)  {
        passwordChangeRequests.add(this.username);
    }

    public void requestAccountRemoval(List <String> accountRemovalRequests) {
        accountRemovalRequests.add(this.username);
    }

}

