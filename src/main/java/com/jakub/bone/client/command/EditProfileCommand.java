package com.jakub.bone.client.command;

import com.jakub.bone.ui.Screen;
import com.jakub.bone.ui.UserInput;

import java.io.IOException;

public class EditProfileCommand implements Command {
    private UserInput input;

    public EditProfileCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandMessage buildCommandMessage() throws IOException {
        Screen.printEditScreen();
        String subCommand = input.getRequest().trim().toUpperCase();
        // Zawsze wysyłamy główny typ EDIT wraz z podkomendą
        CommandMessage.Builder builder = new CommandMessage.Builder()
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
                builder.addPayload("username", username)
                        .addPayload("newRole", newRole);
            }
            case "REMOVE", "SWITCH" -> {
                String username = input.promptUsername();
                builder.addPayload("username", username);
            }
            default -> {
                builder.addPayload("error", "Nieznana operacja: " + subCommand);
            }
        }
        return builder.build();
    }
}
