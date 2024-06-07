package request;

public class CredentialRequest extends Request {

    // Creates register and login request
    public CredentialRequest(String requestCommand, String username, String password) {
        super(requestCommand);
        this.username = username;
        this.password = password;
    }
}
