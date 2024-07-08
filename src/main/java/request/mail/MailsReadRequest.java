package request.mail;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class MailsReadRequest extends Request {
    public MailsReadRequest(String boxOperation, String boxType) {
        setCommand(boxOperation);
        setBoxType(boxType);
        log.info("Read mails request created with operation: {} for boxType: {}", boxOperation, boxType);
    }
}
