package request;

public class WriteRequest extends Request{
    public WriteRequest(String request, String recipient, String message) {
        this.requestCommand = request;
        this.recipient = recipient;
        this.message = message;
    }
}
