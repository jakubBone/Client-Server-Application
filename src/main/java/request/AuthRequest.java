package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthRequest extends Request {

    public AuthRequest(String requestCommand, String username, String password) {
        super(requestCommand);
        this.username = username;
        this.password = password;
        log.info("CredentialRequest created for user: {}", username);
    }
}
