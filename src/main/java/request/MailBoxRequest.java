package request;

public class MailBoxRequest extends Request {
    public MailBoxRequest(String requestCommand, String boxOperation, String mailbox) {
        super(requestCommand);
        this.boxOperation = boxOperation;
        this.mailbox = mailbox;
    }
}