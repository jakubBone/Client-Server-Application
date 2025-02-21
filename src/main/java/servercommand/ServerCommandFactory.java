package servercommand;

import service.AuthService;
import service.MailService;
import response.mail.NewMailServerCommand;
import response.user.EditServerCommand;
import server.ServerDetails;
import service.UserService;

public class ServerCommandFactory {
    private final AuthService authManager;
    private final UserService userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;

    public ServerCommandFactory(AuthService authManager, UserService userManager, MailService mailService, ServerDetails serverDetails) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.serverDetails = serverDetails;
    }

    public ServerCommand createCommand(String request)  {
        switch (request.toUpperCase()) {
            case "REGISTER", "LOGIN" -> { return new AuthServerCommand(authManager, userManager); }
            case "LOGOUT" -> { return new LogoutServerCommand(userManager); }
            case "HELP", "INFO", "UPTIME" -> { return new ServerDetailsCommand(); }
            case "NEW" -> return new NewMailServerCommand(mailService, userManager);
            case "INBOX" -> { return new InboxServerCommand(mailService); }
            case "SENT" -> { return new SentServerCommand(mailService); }
            case "DELETE" -> { return new DeleteMailServerCommand(mailService); }
            case "CHANGE", "REMOVE", "ROLE", "SWITCH" -> { return new EditServerCommand(userManager); }


            default:
                log.warn("Unknown operation: {}", request);
                return null;
        }
    }
}
