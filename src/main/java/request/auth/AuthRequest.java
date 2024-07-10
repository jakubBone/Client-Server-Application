package request.auth;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class AuthRequest extends Request {
    public AuthRequest (String requestCommand, String username, String password) {
        setCommand(requestCommand);
        setUsername(username);
        setPassword(password);
    }
}
