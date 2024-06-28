package user;

import mail.Mail;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.mindrot.jbcrypt.BCrypt;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

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

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        log.info("User instance created: {}", username);
    }

    /*
    BCrypt.checkpw() check whether hashed 'typedPassword' matches with 'hashedPassword'
    Bcrypt uses salt and protects against attacks, ensuring unique hashes even for identical passwords is
    */
    public boolean checkPassword(String typedPassword, DSLContext create) {
        log.info("Checking password for user: {}", username);
        Record record = create.selectFrom("users")
                            .where(DSL.field("username").eq(username))
                            .fetchOne();

        String hashed = record.getValue("hashed_password", String.class);
        return BCrypt.checkpw(typedPassword, hashed);
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