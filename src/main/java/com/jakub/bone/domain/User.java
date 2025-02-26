package com.jakub.bone.domain;

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

    private String username;
    private  String password;
    private  String hashedPassword;
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void hashPassword() {
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        log.info("Password hashed for user: {}", username);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        hashPassword();
        log.info("New password set for user: {}", username);
    }

    public String toString() {
        return username;
    }
}