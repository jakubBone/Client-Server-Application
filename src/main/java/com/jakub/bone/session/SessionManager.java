package com.jakub.bone.session;

import com.jakub.bone.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() { }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == User.Role.ADMIN;
    }
}
