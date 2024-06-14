package handler.auth;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

@Log4j2
public class LogoutHandler {
    public String getResponse(UserManager userManager)  {
        log.info("Attempting to log out user: {}", UserManager.currentLoggedInUser.getUsername());
        return userManager.getLogoutResponse();
    }
}
