package command;

public class UptimeCommand implements Command {
    @Override
    public String execute() {
        // Przykładowy uptime – w praktyce pobierany z obiektu ServerDetails
        return "Uptime: 0 days, 0 hours, 5 minutes, 30 seconds";
    }
}
