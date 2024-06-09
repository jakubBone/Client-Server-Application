package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WriteRequest extends Request {
    public WriteRequest(String requestCommand, String recipient, String message) {
        super(requestCommand);
        this.recipient = recipient;
        this.message = message;
        log.info("WriteRequest created for recipient: {}", recipient);
    }
}
