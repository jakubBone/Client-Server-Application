package request.mail;
import lombok.extern.log4j.Log4j2;
import request.Request;

@Log4j2
public class MailWriteRequest extends Request {
    public MailWriteRequest(String requestCommand, String recipient, String message) {
        setCommand(requestCommand);
        setRecipient(recipient);
        setMessage(message);
    }
}
