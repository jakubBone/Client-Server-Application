package com.jakub.bone.session;

import com.jakub.bone.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private User currentUser;

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        return instance;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.getRole() == User.Role.ADMIN;
    }

    public void logout() {
        currentUser = null;
    }
}
