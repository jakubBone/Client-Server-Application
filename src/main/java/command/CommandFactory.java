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
    private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;
    private final List<User> users;

    public CommandFactory(UserInput userInput, AuthManager authManager, UserManager userManager, MailService mailService, , List<User> users) {
        this.userInput = userInput;
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.users = users;
    }

    public Command createCommand(String commandName) throws IOException {
        switch (commandName.toUpperCase()) {
            case "LOGIN", "REGISTER" -> {
                String username = userInput.promptUsername();
                String password = userInput.promptPassword();
                return new AuthorizationCommand(username, password, authManager, userManager);
            }
            case "LOGOUT" -> { return new LogoutCommand(userManager); }
            case "UPTIME", "INFO", "HELP" -> { return new ServerDetailsCommand(commandName); }
            case "NEW" -> {
                String recipient = userInput.promptRecipient();
                String message = userInput.promptMessage();
                return new NewMailCommand(recipient, message, mailService, userManager);
            }
            case "INBOX" -> { return new InboxCommand(mailService); }
            case "SENT" -> { return new SentCommand(mailService); }
            case "DELETE" -> { return new DeleteCommand(); }
            case "EDIT" -> { return new EditProfileCommand(userInput, users) }
            default:
                log.warn("Unknown operation: {}", command);
                return null;
        }
    }
}
