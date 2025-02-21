package command;

import user.manager.AuthManager;
import user.manager.UserManager;

public class AuthCommand implements Command {
    private final String command;
    private final String username;
    private final String password;

    public AuthCommand(String command, String username, String password) {
        this.command = command;
        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {
        if("REGISTER".equals(command)){

        } else{

        }
    }
}
