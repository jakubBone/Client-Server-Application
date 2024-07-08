package response;

import response.auth.LoginResponse;
import response.auth.LogoutResponse;
import response.auth.RegisterResponse;
import response.mail.MailsDeleteResponse;
import response.mail.MailsReadResponse;
import response.mail.MailWriteResponse;
import response.server.ServerDetailsResponse;
import response.user.UserPasswordChangeResponse;
import response.user.UserRoleChangeResponse;
import response.user.UserRemoveResponse;
import response.user.UserSwitchResponse;
import mail.MailService;
import server.ServerDetails;
import user.manager.AuthManager;
import user.manager.UserManager;

public class ResponseFactory {
    private final AuthManager authManager;
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
                return new RegisterResponse(authManager, userManager);
            case "LOGIN":
                return new LoginResponse(authManager, userManager);
            case "LOGOUT":
                return new LogoutResponse(userManager);
            case "WRITE":
                return new MailWriteResponse(mailService, userManager);
            case "READ":
                return new MailsReadResponse(mailService);
            case "DELETE":
                return new MailsDeleteResponse(mailService);
            case "PASSWORD":
                return new UserPasswordChangeResponse(userManager);
            case "REMOVE":
                return new UserRemoveResponse(userManager);
            case "ROLE":
                return new UserRoleChangeResponse(userManager);
            case "SWITCH":
                return new UserSwitchResponse(userManager);
            case "HELP":
            case "INFO":
            case "UPTIME":
                return new ServerDetailsResponse(serverDetails);
            default:
                throw new IllegalArgumentException("Unknown command type: " + request);
        }
    }
}
