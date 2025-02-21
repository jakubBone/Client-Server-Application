package command;

public class ServerDetailsCommand implements Command{
    private final String command;

    public ServerDetailsCommand(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        if(command.equals("UPTIME")){
            return "Uptime: 0 days, 0 hours, 5 minutes, 30 seconds";
        } else if (command.equals("INFO")) {
            return "Server Info: wersja 1.0.0, uruchomiony 2025-02-21 10:00:00";
        } else {
            return "Server Info: wersja 1.0.0, uruchomiony 2025-02-21 10:00:00";
        }
    }
}
