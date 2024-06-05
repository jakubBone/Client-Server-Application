package operations;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

@Log4j2
public class CredentialHandler {

    private UserManager userManager = new UserManager();

     public String getCredentialResponse(String requestCommand, String username, String password)  {
        String response = null;
        switch (requestCommand) {
            case "REGISTER":
                response = userManager.getRegisterResponse(username, password);
                break;
            case "LOGIN":
                response = userManager.getLoginResponse(username, password);
                break;
        }
        return response;
    }

    public String getLogoutResponse()  {
        return userManager.getLogoutResponse();
    }
}
