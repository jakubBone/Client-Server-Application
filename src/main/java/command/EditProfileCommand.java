package command;

import ui.Screen;
import ui.UserInput;
import user.credential.User;

import java.util.List;

public class EditProfileCommand implements Command {
    private final UserInput userInput;
    private final List<User> users;

    public EditProfileCommand(UserInput userInput, List<User> users) {
        this.userInput = userInput;
        this.users = users;
    }

    @Override
    public String execute() {
        Screen.printEditScreen();
        String choice = userInput.getRequest();
        Screen.printUsers(users);
        switch (choice) {
            case "CHANGE":
                return "Hasło zostało zmienione.";
            case "ASSIGN":
                return "Rola została przypisana.";
            case "REMOVE":
                return "Użytkownik został usunięty.";
            case "SWITCH":
                return "Przełączono użytkownika.";
            default:
                return "Powrót do menu.";
        }
    }
}
