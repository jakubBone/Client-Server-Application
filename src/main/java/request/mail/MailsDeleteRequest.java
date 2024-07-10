package request.mail;
import lombok.extern.log4j.Log4j2;
import request.Request;
@Log4j2
public class MailsDeleteRequest extends Request {
    public MailsDeleteRequest(String boxOperation, String boxType) {
        setCommand(boxOperation);
        setBoxType(boxType);
    }
}
