package request;

public class CredentialRequest extends Request {
    public CredentialRequest(String request, String username, String password) {
        super(request);
        this.username = username;
        this.password = password;
    }
    public CredentialRequest(String request) {
        super(request);
    }
}
