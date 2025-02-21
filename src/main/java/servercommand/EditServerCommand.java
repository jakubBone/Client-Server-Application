package servercommand;

import command.CommandMessage;
import user.credential.User;
import user.manager.UserManager;

import java.util.Map;

public class EditServerCommand implements ServerCommand{
    private final UserManager userManager;

    public EditServerCommand(UserManager userManager) {
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
                    return "User not found: " + username;
                }
                userManager.changePassword(user, newPassword);
                return "Password changed successfully for " + username;
            }
            case "ASSIGN" -> {
                String username = (String) commandMessage.getPayload().get("username");
                String newRoleStr = (String) commandMessage.getPayload().get("newRole");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return "User not found: " + username;
                }
                try {
                    User.Role newRole = User.Role.valueOf(newRoleStr.toUpperCase());
                    userManager.changeUserRole(user, newRole);
                    return "Role changed successfully for " + username;
                } catch (IllegalArgumentException e) {
                    return "Invalid role specified: " + newRoleStr;
                }
            }
            case "REMOVE" -> {
                String username = (String) commandMessage.getPayload().get("username");
                User user = userManager.getUserByUsername(username);
                if (user == null) {
                    return "User not found: " + username;
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
}
