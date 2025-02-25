package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.domain.User;
import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class EditProfileCommand implements Command {
    private UserInput input;

    public EditProfileCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        Screen.printEditScreen();
        String subCommand = input.getRequest().trim().toUpperCase();

        while (!isValidSubCommand(subCommand)) {
            Screen.printEditScreen();
            subCommand = input.getRequest().trim().toUpperCase();;
        }

        CommandDTO.Builder builder = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", subCommand);

        switch (subCommand) {
            case "CHANGE" -> {
                String username = input.promptUsername();
                String newPassword = input.promptNewPassword();
                builder.addPayload("username", username)
                        .addPayload("newPassword", newPassword);
            }
            case "ASSIGN" -> {
                String username = input.promptUsername();
                String newRole = input.promptNewRole();
                while (!isValidRole(newRole)) {
                    newRole = input.promptNewRole();
                }
                builder.addPayload("username", username)
                        .addPayload("newRole", newRole);
            }
            case "REMOVE", "SWITCH" -> {
                String username = input.promptUsername();
                builder.addPayload("username", username);
            }
            default -> {
                builder.addPayload("error", "Unknown operation: " + subCommand);
                return null;
            }
        }
        return builder.build();
    }

    private boolean isValidSubCommand(String subCommand) {
        return subCommand != null && subCommand.equals("CHANGE") ||
                subCommand.equals("ASSIGN") ||
                subCommand.equals("REMOVE") ||
                subCommand.equals("SWITCH");
    }

    private boolean isValidRole(String newRole) {
        return (newRole != null && newRole.equalsIgnoreCase("USER")
                || newRole.equalsIgnoreCase("ADMIN"));
    }
}
