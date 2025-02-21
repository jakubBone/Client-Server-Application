package command;

import mail.MailService;
import ui.UserInput;
import user.credential.User;
import user.manager.AuthManager;
import user.manager.UserManager;

import java.io.IOException;
import java.util.List;

public class CommandFactory {
    private final UserInput userInput;

    public CommandFactory(UserInput userInput) {
        this.userInput = userInput;
    }

    public Command createCommand(String command) throws IOException {
        switch (command.toUpperCase()) {
            case "LOGIN", "REGISTER" -> {
                String username = userInput.promptUsername();
                String password = userInput.promptPassword();
                return new AuthCommand(command, username, password);
            }
            case "LOGOUT" -> {
                return new LogoutCommand();
            }
            case "UPTIME", "INFO", "HELP" -> {
                return new ServerDetailsCommand(command);
            }
            case "NEW" -> {
                String recipient = userInput.promptRecipient();
                String message = userInput.promptMessage();
                return new NewMailCommand(recipient, message);
            }
            case "INBOX" -> {
                return new InboxCommand();
            }
            case "SENT" -> {
                return new SentCommand();
            }
            case "DELETE" -> {
                return new DeleteMailCommand();
            }
            case "EDIT" -> {
                return new EditProfileCommand(userInput)
            }
            default:
                log.warn("Unknown operation: {}", command);
                return null;
        }
    }
}
