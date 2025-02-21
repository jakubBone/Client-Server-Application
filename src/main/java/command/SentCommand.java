package command;

import mail.MailService;

public class SentCommand implements Command {
    private final MailService mailService;

    public SentCommand(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute() {

    }
}
