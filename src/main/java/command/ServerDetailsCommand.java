package command;

public class ServerDetailsCommand implements Command{
    private final String command;

    public ServerDetailsCommand(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        if("UPTIME".equals(command)){

        } else if ("INFO".equals(command)) {

        } else {

        }
    }
}
