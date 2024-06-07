package handler;

import user.UserManager;
public class LogoutHandler {

    public String getResponse(UserManager userManager)  {
        return userManager.getLogoutResponse();
    }
}
