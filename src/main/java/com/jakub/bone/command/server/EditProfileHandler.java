package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.domain.User;
import com.jakub.bone.application.UserService;
import com.jakub.bone.session.SessionManager;
import com.jakub.bone.utils.*;

public class EditProfileHandler implements CommandHandler {
    private final UserService userManager;

    public EditProfileHandler(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String subCommand = commandDTO.getPayload().get("subCommand");
        String username = commandDTO.getPayload().get("username");

        User user = userManager.findUserByUsername(username);
        if (user == null) {
            return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
        }

        switch (subCommand.toUpperCase()) {
            case "CHANGE" -> {
                String newPassword = commandDTO.getPayload().get("newPassword");
                userManager.changePassword(user, newPassword);
                return ResponseStatus.OPERATION_SUCCEEDED.getResponse();
            }
            case "ASSIGN" -> {
                String newRole = commandDTO.getPayload().get("newRole");
                User.Role role = User.Role.valueOf(newRole.toUpperCase());
                userManager.changeUserRole(user, role);
                return ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse();

            }
            case "REMOVE" -> {
                userManager.removeUser(user);
                return ResponseStatus.USER_DELETE_SUCCEEDED.getResponse();
            }
            case "SWITCH" -> {
                userManager.switchUser(user);
                if(SessionManager.getInstance().isAdmin()){
                    return ResponseStatus.ADMIN_SWITCH_SUCCEEDED.getResponse();
                } else {
                    return ResponseStatus.USER_SWITCH_SUCCEEDED.getResponse();
                }

            }
            default -> {
               return ResponseStatus.UNKNOWN_REQUEST.getResponse();
            }
        }
    }
}
