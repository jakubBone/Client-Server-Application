package com.jakub.bone.session;

import com.jakub.bone.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionManager {
    private User currentUser;

    public SessionManager() { }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == User.Role.ADMIN;
    }
}
