package command;

import ui.Screen;
import ui.UserInput;

public class EditProfileCommand implements Command {
    private final UserInput userInput;
    public EditProfileCommand(UserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public String execute() {
        Screen.printEditScreen();
        String choice = userInput.getRequest();
        switch (choice) {
            case "CHANGE":

            case "ASSIGN":

            case "REMOVE":

            case "SWITCH":

            default:

        }
    }
}
