package servercommand;

import command.CommandMessage;
import mail.MailService;
import user.manager.UserManager;

public class NewMailServerCommand implements ServerCommand {
    private final MailService mailService;
    private final UserManager userManager;

    public NewMailServerCommand(MailService mailService, UserManager userManager) {
        this.mailService = mailService;
        this.userManager = userManager;
    }

    @Override
    public String execute(CommandMessage commandMessage) {
        String recipient = (String) commandMessage.getPayload().get("recipient");
        String message = (String) commandMessage.getPayload().get("message");
        return mailService.sendMail(userManager.getUserByUsername(recipient), message);
    }
}
