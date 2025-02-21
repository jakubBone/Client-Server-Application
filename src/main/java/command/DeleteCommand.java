package command;

public class DeleteCommand implements Command {
    private final MailService mailService;
    private final String boxType; // np. "INBOX" lub "SENT", zależnie od tego, z której skrzynki chcemy usuwać

    public DeleteMailCommand(MailService mailService, String boxType) {
        this.mailService = mailService;
        this.boxType = boxType;
    }

    @Override
    public String execute() {
        // Usuwamy wiadomości z podanej skrzynki
        mailService.deleteMails(boxType);
        return "Usunięto wiadomości ze skrzynki: " + boxType;
    }
}
