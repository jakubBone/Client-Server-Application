package command;

import mail.MailService;
import ui.UserInput;
import user.manager.UserManager;

public class NewMailCommand implements Command{
    private final String recipient;
    private final String message;
    private final MailService mailService;
    private final UserManager userManager;

    public NewMailCommand(String recipient, String message, MailService mailService, UserManager userManager) {
        this.recipient = recipient;
        this.message = message;
        this.mailService = mailService;
        this.userManager = userManager;
    }

    @Override
    public String execute() {
        if (userManager.getUserByUsername(recipient) == null) {
            return "Nie znaleziono odbiorcy.";
        }
        mailService.sendMail(userManager.getUserByUsername(recipient), message);
        return "Wiadomość wysłana do: " + recipient;
    }
}
