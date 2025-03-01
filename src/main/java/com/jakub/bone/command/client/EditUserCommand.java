package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;
import java.util.List;

import static com.jakub.bone.ui.Screen.printEditScreen;

public class EditUserCommand implements Command {
    private UserInput input;

    public EditUserCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandDTO buildCommandMessage() throws IOException {
        String subCommand = null;
        while (!isValidSubCommand(subCommand)) {
            printEditScreen();
            subCommand = input.getRequest().trim().toUpperCase();;
        }

        CommandDTO.Builder builder = new CommandDTO.Builder()
                .commandType("EDIT")
                .addPayload("subCommand", subCommand);

        switch (subCommand) {
            case "CHANGE" -> handleChange(builder);
            case "ASSIGN" -> handleAssign(builder);
            case "REMOVE", "SWITCH" -> builder.addPayload("username", input.promptUsername());
            default -> {
                builder.addPayload("error", "Unknown operation: " + subCommand);
                return null;
            }
        }
        return builder.build();
    }

    private void handleChange(CommandDTO.Builder builder) throws IOException {
        String username = input.promptUsername();
        String newPassword = input.promptNewPassword();
        builder.addPayload("username", username)
                .addPayload("newPassword", newPassword);
    }

    private void handleAssign(CommandDTO.Builder builder) throws IOException {
        String username = input.promptUsername();
        String newRole = input.promptNewRole();
        while (!isValidRole(newRole)) {
            newRole = input.promptNewRole();
        }
        builder.addPayload("username", username)
                .addPayload("newRole", newRole);
    }

    private boolean isValidSubCommand(String subCommand) {
        return subCommand != null && List.of("CHANGE", "ASSIGN", "REMOVE", "SWITCH").contains(subCommand);
    }

    private boolean isValidRole(String newRole) {
        return newRole != null && List.of("USER", "ADMIN").contains(newRole.toUpperCase());
    }

    /*private boolean isValidSubCommand(String subCommand) {
        return subCommand != null (&& subCommand.equals("CHANGE") ||
                subCommand.equals("ASSIGN") ||
                subCommand.equals("REMOVE") ||
                subCommand.equals("SWITCH"));
    }*/



    /*private boolean isValidRole(String newRole) {
        return (newRole != null && newRole.equalsIgnoreCase("USER")
                || newRole.equalsIgnoreCase("ADMIN"));
    }*/
}
