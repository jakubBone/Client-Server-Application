package handler.auth;

import lombok.extern.log4j.Log4j2;
import shared.ResponseMessage;
import user.manager.AuthManager;
import user.manager.UserManager;

/**
 * The AuthHandler class processes authentication requests such as REGISTER and LOGIN.
 * It interacts with the UserManager to handle these operations and generate appropriate responses.
 */

@Log4j2
public class AuthHandler {

    AuthManager authManager = new AuthManager();

    /**
     * Processes authentication requests (REGISTER, LOGIN) and generates appropriate responses.
     * @param requestCommand The command for the request (e.g., "REGISTER", "LOGIN")
     */
     public String getResponse(String requestCommand, String username, String password, UserManager userManager)  {
         log.info("Processing authentication request: {}", requestCommand);
         switch (requestCommand) {
            case "REGISTER":
                return authManager.registerAndGetResponse(username, password, userManager);
            case "LOGIN":
                return authManager.loginAndGetResponse(username, password, userManager);
             default:
                 log.warn("Unknown request: {}", requestCommand);
                 return ResponseMessage.UNKNOWN_REQUEST.getResponse();
        }
    }
}