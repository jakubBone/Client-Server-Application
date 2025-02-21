package command;

import ui.Screen;
import ui.UserInput;

import java.io.IOException;

public class EditProfileCommand implements Command {
    private UserInput input;

    public EditProfileCommand(UserInput input) {
        this.input = input;
    }

    @Override
    public CommandMessage buildCommandMessage() throws IOException {
        Screen.printEditScreen();
        String subCommand = input .getRequest();
        switch (subCommand) {
            case "CHANGE" -> {
                String username = input.promptUsername();
                String newPassword = input .promptNewPassword();
                return new CommandMessage.Builder()
                        .commandType("CHANGE")
                        .addPayload("username", username)
                        .addPayload("newPassword", newPassword)
                        .build();
            }
            case "ASSING" -> {
                String username = input.promptUsername();
                String newRole = input .promptNewRole();
                return new CommandMessage.Builder()
                        .commandType("ASSIGN")
                        .addPayload("username", username)
                        .addPayload("newRole", newRole)
                        .build()
            }
            case "REMOVE" -> {
                String username = input.promptUsername();
                return new CommandMessage.Builder()
                        .commandType("REMOVE")
                        .addPayload("username", username)
                        .build()
            }
            case "SWITCH" -> {
                String username = input.promptUsername();
                return  new CommandMessage.Builder()
                        .commandType("SWITCH")
                        .addPayload("username", username)
                        .build()
            }
            default:
        }
    }
}
