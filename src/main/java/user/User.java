package user;

import mail.MailBox;
import org.mindrot.jbcrypt.BCrypt;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class User {

    public enum Role {
        ADMIN,
        USER;
    }
    protected String username;
    protected String password;
    protected String hashedPassword;
    protected Role role;
    protected MailBox mailBox;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mailBox = new MailBox();
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        log.info("User instance created: {}", username);
    }

    /*
    BCrypt.checkpw() check whether hashed 'typedPassword' matches with 'hashedPassword'
    Bcrypt uses salt and protects against attacks, ensuring unique hashes even for identical passwords is
    */
    public boolean checkPassword(String typedPassword) {
        log.info("Checking password for user: {}", username);
        return BCrypt.checkpw(typedPassword, hashedPassword);
    }

    public void hashPassword(){
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        log.info("Password hashed for user: {}", username);
    }

    public void setPassword(String newPassword) {
        log.info("Setting new password for user: {}", username);
        this.password = newPassword;
        hashPassword();
        log.info("New password set for user: {}", username);
    }

    public String toString() {
        return username;
    }
}