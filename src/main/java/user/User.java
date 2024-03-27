package user;

import mail.MailBox;
import user.Role;

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
        mailBox = new MailBox();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }
}

