package request;

public class MailBoxRequest extends Request{
    public MailBoxRequest(String request, String boxOperation) {
        this.requestCommand = request;
        this.boxOperation = boxOperation;
    }
}
