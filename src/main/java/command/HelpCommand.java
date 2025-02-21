package command;

public class HelpCommand implements Command{
    @Override
    public String execute() {
        return "Dostępne komendy: LOGIN, REGISTER, UPTIME, INFO, HELP, EXIT";
    }
}
