package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.domain.User;
import com.jakub.bone.application.UserService;
import lombok.extern.log4j.Log4j2;

import static com.jakub.bone.utils.ResponseStatus.*;
@Log4j2
public class EditUserHandler implements CommandHandler {
    private final UserService userManager;

    public EditUserHandler(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String subCommand = commandDTO.getPayload().get("subCommand");
        String username = commandDTO.getPayload().get("username");

        User user = userManager.findUserByUsername(username);
        if (user == null) {
            return FAILED_TO_FIND_USER.getResponse();
        }

        switch (subCommand.toUpperCase()) {
            case "CHANGE" -> {
                String newPassword = commandDTO.getPayload().get("newPassword");
                userManager.changePassword(user, newPassword);
                return OPERATION_SUCCEEDED.getResponse();
            }
            case "ASSIGN" -> {
                String newRole = commandDTO.getPayload().get("newRole");
                User.Role role = User.Role.valueOf(newRole.toUpperCase());
                userManager.changeUserRole(user, role);
                return ROLE_CHANGE_SUCCEEDED.getResponse();

            }
            case "REMOVE" -> {
                userManager.removeUser(user);
                return USER_DELETE_SUCCEEDED.getResponse();
            }
            case "SWITCH" -> {
                userManager.switchUser(user);
                if(userManager.getSessionManager().isAdmin()){
                    return ADMIN_SWITCH_SUCCEEDED.getResponse();
                } else {
                    return USER_SWITCH_SUCCEEDED.getResponse();
                }

            }
            default -> {
                log.warn("Unknown operation: {}", subCommand);
                return UNKNOWN_REQUEST.getResponse();
            }
        }
    }
}
