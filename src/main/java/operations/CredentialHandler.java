package operations;

import lombok.extern.log4j.Log4j2;
import user.UserManager;

import java.io.IOException;

@Log4j2
public class CredentialHandler {

     public String getCredentialResponse(String command, String username, String password, UserManager userManager) throws IOException {
        String response = null;
        switch (command) {
            case "REGISTER":
                response = userManager.getRegisterResponse(username, password);
                break;
            case "LOGIN":
                response = userManager.getLoginResponse(username, password);
                System.out.println(UserManager.currentLoggedInUser.getRole());
                break;
            case "LOGOUT":
                response = userManager.getLogoutResponse();
                break;
        }
        return response;
    }
}
