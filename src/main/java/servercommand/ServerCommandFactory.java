package servercommand;

import mail.MailService;
import response.mail.InboxServerCommand;
import response.mail.NewMailServerCommand;
import response.mail.DeleteMailServerCommand;
import response.server.UptimeServerCommand;
import response.user.EditServerCommand;
import response.user.UserRemoveResponse;
import response.user.UserRoleChangeResponse;
import response.user.UserSwitchResponse;
import server.ServerDetails;
import user.manager.AuthManager;
import user.manager.UserManager;

public class ServerCommandFactory {
    private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;

    public ServerCommandFactory(AuthManager authManager, UserManager userManager, MailService mailService, ServerDetails serverDetails) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.serverDetails = serverDetails;
    }

    public ServerCommand createCommand(String request)  {
        switch (request.toUpperCase()) {
            case "REGISTER", "LOGIN" -> { return new AuthServerCommand(authManager, userManager); }
            case "LOGOUT" -> { return new LogoutServerCommand(userManager); }
            case "HELP", "INFO", "UPTIME" -> { return new UptimeServerCommand(serverDetails); }
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
