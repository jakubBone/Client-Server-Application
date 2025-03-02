package com.jakub.bone.command.client;

import com.jakub.bone.command.common.Command;
import com.jakub.bone.command.common.CommandDTO;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, String> payload = new HashMap<>();
        payload.put("subCommand", subCommand);

        switch (subCommand) {
            case "CHANGE" -> handleChange(payload);
            case "ASSIGN" -> handleAssign(payload);
            case "REMOVE", "SWITCH" -> payload.put("username", input.promptUsername());
            default -> payload.put("errorCode", "UNKNOWN");
        }

        return CommandDTO.builder()
                .commandType("EDIT")
                .payload(payload)
                .build();
    }

    private void handleChange(Map<String, String> payload) throws IOException {
        String username = input.promptUsername();
        String newPassword = input.promptNewPassword();
        payload.put("username", username);
        payload.put("newPassword", newPassword);
    }

    private void handleAssign(Map<String, String> payload) throws IOException {
        String username = input.promptUsername();
        String newRole = input.promptNewRole();
        while (!isValidRole(newRole)) {
            newRole = input.promptNewRole();
        }
        payload.put("username", username);
        payload.put("newRole", newRole);
    }

    private boolean isValidSubCommand(String subCommand) {
        return subCommand != null && List.of("CHANGE", "ASSIGN", "REMOVE", "SWITCH").contains(subCommand);
    }

    private boolean isValidRole(String newRole) {
        return newRole != null && List.of("USER", "ADMIN").contains(newRole.toUpperCase());
    }
}
