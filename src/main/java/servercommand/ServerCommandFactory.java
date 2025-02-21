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
    /*private final AuthManager authManager;
    private final UserManager userManager;
    private final MailService mailService;

    public ServerCommandFactory(AuthManager authManager, UserManager userManager, MailService mailService) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.mailService = mailService;
    }

    public ServerCommand createCommand(String request) throws IOException {
        String cmd = request
        // Przykładowa implementacja dla logowania
        if (cmd.equalsIgnoreCase("LOGIN")) {
            // Zakładamy, że payload zawiera "username:password"
            String[] parts = request.getPayload().split(":");
            if(parts.length < 2) {
                return () -> "Invalid payload for LOGIN";
            }
            return new LoginServerCommand(parts[0], parts[1], authManager, userManager);
        }
        // Analogicznie możesz dodać kolejne komendy, np. dla rejestracji, wysyłania maila, itd.
        // Dla nieznanych komend zwracamy domyślną komendę:
        return () -> "Unknown command";
    }*/

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
            case "REGISTER", "LOGIN" -> { return new RegisterCommand(authManager, userManager); }
            case "LOGOUT" -> { return new LogoutCommand (userManager); }

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
    }
}
