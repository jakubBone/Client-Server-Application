package com.jakub.bone.server.command;

import com.jakub.bone.client.command.CommandMessage;
import com.jakub.bone.domain.model.User;
import com.jakub.bone.application.service.UserService;
import com.jakub.bone.utils.*;

public class EditServerCommand implements ServerCommand{
    private final UserService userManager;

    public EditServerCommand(UserService userManager) {
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String subCommand = (String) commandMessage.getPayload().get("subCommand");
        switch (subCommand.toUpperCase()) {
            case "CHANGE" -> {
                String username = (String) commandMessage.getPayload().get("username");
                String newPassword = (String) commandMessage.getPayload().get("newPassword");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    //return "User not found: " + username;
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                userManager.changePassword(user, newPassword);
                return ResponseStatus.OPERATION_SUCCEEDED.getResponse();
                return "Password changed successfully for " + username;
            }
            case "ASSIGN" -> {
                String username = (String) commandMessage.getPayload().get("username");
                String newRoleStr = (String) commandMessage.getPayload().get("newRole");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                try {
                    User.Role newRole = User.Role.valueOf(newRoleStr.toUpperCase());
                    userManager.changeUserRole(user, newRole);
                    return ResponseStatus.ROLE_CHANGE_SUCCEEDED.getResponse()
                } catch (IllegalArgumentException e) {
                    return "Invalid role specified: " + newRoleStr;
                }
            }
            case "REMOVE" -> {
                String username = (String) commandMessage.getPayload().get("username");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return ResponseStatus.FAILED_TO_FIND_USER.getResponse();
                }
                userManager.removeUser(user);
                return "User " + username + " removed successfully.";
            }
            case "SWITCH" -> {
                String username = (String) commandMessage.getPayload().get("username");
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
