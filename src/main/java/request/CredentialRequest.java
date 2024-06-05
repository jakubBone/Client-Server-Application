package request;

public class CredentialRequest extends Request {

    // Creates register and login request
    public CredentialRequest(String request, String username, String password) {
        super(request);
        this.username = username;
        this.password = password;
    }

    // Creates logout request
    public CredentialRequest(String requestCommand) {
        super(requestCommand);
    }
}
