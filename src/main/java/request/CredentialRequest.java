package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CredentialRequest extends Request {

    public CredentialRequest(String requestCommand, String username, String password) {
        super(requestCommand);
        this.username = username;
        this.password = password;
        log.info("CredentialRequest created for user: {}", username);
    }
}
