package command;

public class InfoCommand extends Command {
    @Override
    public String execute() {
        // Przykładowe informacje serwera
        return "Server Info: wersja 1.0.0, uruchomiony 2025-02-21 10:00:00";
    }
}
