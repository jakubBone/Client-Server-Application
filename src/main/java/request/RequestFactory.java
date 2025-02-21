package request;

import lombok.extern.log4j.Log4j2;

import request.auth.AuthRequest;
import request.auth.LogoutRequest;
import request.mail.DeleteMailRequest;
import request.mail.InboxRequest;
import request.mail.NewMailRequest;
import request.mail.ServerDetailsRequest;
import request.user.AssignRoleRequest;
import request.user.ChangePasswordRequest;
import request.user.RemoveUserRequest;
import request.user.SwitchUserRequest;

import java.io.IOException;

@Log4j2
public class RequestFactory {
    public Request getRequest(String command) throws IOException {
        switch (command.toUpperCase()) {
            case "REGISTER", "LOGIN" -> return new AuthRequest(command);
            case "LOGOUT" -> return new LogoutRequest();
            case "HELP", "INFO", "UPTIME" -> return new ServerDetailsRequest(command);
            case "NEW" -> return new NewMailRequest();
            case "INBOX" -> return new InboxRequest()
            case "SENT" -> return new NewMailRequest()
            case "DELETE" -> return new DeleteMailRequest();
            case "CHANGE" -> return new ChangePasswordRequest();
            case "ASSIGN" -> return new AssignRoleRequest();
            case "REMOVE" -> return new RemoveUserRequest();
            case "SWITCH" -> return  new SwitchUserRequest()
            default:
                log.warn("Unknown operation: {}", command);
                return null;
        }
    }
}