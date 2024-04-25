package request;

public class MailBoxRequest extends Request {
    public MailBoxRequest(String request, String boxOperation) {
        super(request);
        this.boxOperation = boxOperation;
    }
}