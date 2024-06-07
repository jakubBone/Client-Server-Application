package handler;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

@Log4j2
public class AuthHandler {
     public String getResponse(String requestCommand, String username, String password, UserManager userManager)  {
        switch (requestCommand) {
            case "REGISTER":
                return userManager.registerAndGetResponse(username, password);
            case "LOGIN":
                return userManager.loginAndGetResponse(username, password);
        }
        return null;
    }
}
