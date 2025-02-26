package com.jakub.bone.application;

import com.jakub.bone.data.DataSource;
import com.jakub.bone.repository.UserRepository;
import com.jakub.bone.session.SessionManager;
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
    private final SessionManager sessionManager;

    public UserService(AuthService authService, UserRepository userRepository) {

        this.userRepository = userRepository;
        this.admin = new Admin();
        this.authManager = authService;
        this.sessionManager = authService.getSessionManager();
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.warn("User not found in database: {}", username);
        }
        return user;
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userRepository.updateUser(user);
        log.info("Password successfully changed for user: {}", user.getUsername());
    }

    public void removeUser(User user) {
        userRepository.removeUser(user.getUsername());
        log.info("User remove successfully: {}", user.getUsername());
    }

    public void switchUser(User user) {
        sessionManager.setCurrentUser(user);
        log.info("User switched successfully: {}", user.getUsername());
    }

    public void changeUserRole(User user, User.Role role) {
        user.setRole(role);
        userRepository.changeUserRole(user, role);
        log.info("Role changed successfully: {} to {}", user.getUsername(), role);
    }
}