package com.jakub.bone.command.server;

import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.command.server.CommandHandler;
import com.jakub.bone.domain.User;
import com.jakub.bone.application.UserService;
import com.jakub.bone.utils.*;

public class EditProfileHandler implements CommandHandler {
    private final UserService userManager;

    public EditProfileHandler(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandDTO commandDTO) {
        String subCommand = (String) commandDTO.getPayload().get("subCommand");
        switch (subCommand.toUpperCase()) {
            case "CHANGE" -> {
                String username = (String) commandDTO.getPayload().get("username");
                String newPassword = (String) commandDTO.getPayload().get("newPassword");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    //return "User not found: " + username;
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                userManager.changePassword(user, newPassword);
                return ResponseStatus.OPERATION_SUCCEEDED.getResponse();
            }
            case "ASSIGN" -> {
                String username = (String) commandDTO.getPayload().get("username");
                String newRoleStr = (String) commandDTO.getPayload().get("newRole");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                try {
                    User.Role newRole = User.Role.valueOf(newRoleStr.toUpperCase());
                    userManager.changeUserRole(user, newRole);
                    return ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse();
                } catch (IllegalArgumentException e) {
                    return "Invalid role specified: " + newRoleStr;
                }
            }
            case "REMOVE" -> {
                String username = (String) commandDTO.getPayload().get("username");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                userManager.removeUser(user);
                return "User " + username + " removed successfully.";
            }
            case "SWITCH" -> {
                String username = (String) commandDTO.getPayload().get("username");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return "User not found: " + username;
                }
                userManager.switchUser(user);
                return "Switched to user: " + username;
            }
            default -> {
                return "Unknown subCommand: " + subCommand;
            }
        }
    }

    /*if (!userManager.isUserAdmin()) {
            return ResponseStatus.SWITCH_FAILED.getResponse() + ": user not authorized";
        }

        userManager.switchUser(user);

        if (UserService.ifSwitchedToAdminUser) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_ROLE_ADMIN_ROLE.getResponse();
        }

        if(UserService.ifSwitchedToNonAdminUser) {
            return ResponseStatus.SWITCH_SUCCEEDED_USER_NON_ADMIN_ROLE.getResponse();
        }

        return ResponseStatus.SWITCH_FAILED.getResponse();*/
}
