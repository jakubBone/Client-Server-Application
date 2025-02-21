package command;

import mail.MailService;

public class InboxCommand implements Command {
    private final MailService mailService;

    public InboxCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute() {
        // Pobieramy wiadomości z wybranej skrzynki
        return "Wyświetlam wiadomości z " + boxType;
    }
}
