package request;

public class MailBoxRequest extends Request {
    public MailBoxRequest(String request, String boxOperation, String mailbox) {
        super(request);
        this.boxOperation = boxOperation;
        this.mailbox = mailbox;
    }
}