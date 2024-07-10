package request.auth;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class LogoutRequest extends Request {
    public LogoutRequest(String requestCommand) {
        setCommand(requestCommand);
    }
}
