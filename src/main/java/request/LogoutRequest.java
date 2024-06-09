package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogoutRequest extends Request {
    public LogoutRequest(String requestCommand) {
        super(requestCommand);
        log.info("LogoutRequest created with command: {}", requestCommand);
    }
}
