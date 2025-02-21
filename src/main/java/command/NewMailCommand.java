package command;

import mail.MailService;
import ui.UserInput;
import user.manager.UserManager;

public class NewMailCommand implements Command{
    private final String recipient;
    private final String message;

    public NewMailCommand(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;

    }

    @Override
    public String execute() {

    }
}
