package request.mail;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class ServerDetailsRequest extends Request{
    public ServerDetailsRequest(String requestCommand) {
        setCommand(requestCommand);
        log.info("Server details request created with command: {}", requestCommand);
    }
}
