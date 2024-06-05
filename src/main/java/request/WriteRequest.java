package request;

public class WriteRequest extends Request {
    public WriteRequest(String requestCommand, String recipient, String message) {
        super(requestCommand);
        this.recipient = recipient;
        this.message = message;
    }
}
