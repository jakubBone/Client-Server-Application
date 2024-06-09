package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServerDetailsRequest extends Request{
    public ServerDetailsRequest(String requestCommand) {
        super(requestCommand);
        log.info("ServerDetailsRequest created with command: {}", requestCommand);
    }
}
