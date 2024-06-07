package operations;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

@Log4j2
public class CredentialHandler {
     public String getResponse(String requestCommand, String username, String password,
                               UserManager userManager)  {
        String response = null;
        switch (requestCommand) {
            case "REGISTER":
                response = userManager.registerAndGetResponse(username, password);
                break;
            case "LOGIN":
                response = userManager.loginAndGetResponse(username, password);
                break;
        }
        return response;
    }
}
