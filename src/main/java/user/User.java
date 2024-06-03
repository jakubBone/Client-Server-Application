package user;

import mail.MailBox;

import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

@Getter
@Setter
public class User {
    public static User.Role Role;

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
    }

    /*
    BCrypt.checkpw() check whether hashed 'typedPassword' matches with 'hashedPassword'
    Bcrypt uses salt and protects against attacks, ensuring unique hashes even for identical passwords is
    */
    public boolean checkPassword(String typedPassword) {
        return BCrypt.checkpw(typedPassword, hashedPassword);
    }

    public void hashPassword(){
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public String toString() {
        return username;
    }
}