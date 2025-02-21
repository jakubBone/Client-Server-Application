package response;
import lombok.extern.log4j.Log4j2;
import response.auth.LoginServerCommand;
import response.auth.LogoutServerCommand;
import response.auth.RegisterServerCommand;
import response.mail.DeleteMailServerCommand;
import response.mail.InboxServerCommand;
import response.mail.NewMailServerCommand;
import response.server.UptimeServerCommand;
import response.user.EditServerCommand;
import response.user.UserRoleChangeResponse;
import response.user.UserRemoveResponse;
import response.user.UserSwitchResponse;
import mail.MailService;
import server.ServerDetails;
import user.manager.AuthManager;
import user.manager.UserManager;

@Log4j2
public class ResponseFactory {
    /*private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;
    private final ServerDetails serverDetails;

    public ResponseFactory(AuthManager authManager, UserManager userManager, MailService mailService, ServerDetails serverDetails) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
        this.serverDetails = serverDetails;
    }

    public Response getResponse(String request)  {
        switch (request.toUpperCase()) {
            case "REGISTER":
                return new RegisterServerCommand(authManager, userManager);
            case "LOGIN":
                return new LoginServerCommand(authManager, userManager);
            case "LOGOUT":
                return new LogoutServerCommand(userManager);
            case "WRITE":
                return new NewMailServerCommand(mailService, userManager);
            case "READ":
                return new InboxServerCommand(mailService);
            case "DELETE":
                return new DeleteMailServerCommand(mailService);
            case "PASSWORD":
                return new EditServerCommand(userManager);
            case "REMOVE":
                return new UserRemoveResponse(userManager);
            case "ROLE":
                return new UserRoleChangeResponse(userManager);
            case "SWITCH":
                return new UserSwitchResponse(userManager);
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new UptimeServerCommand(serverDetails);
            default:
                log.warn("Unknown operation: {}", request);
                return null;
        }
    }*/
}
