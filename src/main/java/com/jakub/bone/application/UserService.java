package com.jakub.bone.application;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.jakub.bone.domain.Admin;
import com.jakub.bone.domain.User;

@Log4j2
@Getter
@Setter
public class UserService {
    public Admin admin;
    private DSLContext create;
    private UserRepository userRepository;
    private AuthService authManager;

    public UserService() {
        this.create = DSL.using(DataSource.getInstance().getConnection());
        this.userRepository = new UserRepository(create);
        this.admin = new Admin();
        this.authManager = new AuthService();
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.warn("User not found in database: {}", username);
        }
        return user;
    }

    public void changePassword(User user, String newPassword) {
        log.info("Attempting to password change for user: {}", user.getUsername());

        user.setPassword(newPassword);

        log.info("Attempting to upload database: {}", user.getUsername());
        userRepository.updateUser(user);

        log.info("Data base upload succeeded {}", user.getUsername());
        log.info("Password change succeeded for user: {}", user.getUsername());
    }

    public void removeUser(User user) {
        log.info("Attempting to remove user: {}", user.getUsername());

        userRepository.removeUser(user.getUsername());

        log.info("User removal succeeded: {}", user.getUsername());
    }

    public void switchUser(User user) {
        log.info("Attempting to switch to user: {}", user.getUsername());

        log.info("Switched to user: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role) {
        log.info("Attempting to role change for user: {}", user.getUsername());

        user.setRole(role);
        userRepository.changeUserRole(user, role);

        log.info("Role change succeeded for user: {} to {}", user.getUsername(), role);
    }
}