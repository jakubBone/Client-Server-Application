package request;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MailBoxRequest extends Request {
    public MailBoxRequest(String requestCommand, String boxOperation, String mailbox) {
        super(requestCommand);
        this.boxOperation = boxOperation;
        this.mailbox = mailbox;
        log.info("MailBoxRequest created with operation: {} for mailbox: {}", boxOperation, mailbox);
    }
}