package response.mail;

import response.Response;
import mail.MailService;
import request.Request;
import shared.ResponseStatus;

public class MailsDeleteResponse implements Response {
    private final MailService mailService;

    public MailsDeleteResponse(MailService mailService) {
        this.mailService = mailService;
    }
    @Override
    public String execute(Request request) {
        String boxType = request.getBoxType();
        mailService.deleteMails(boxType);
        return ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse();
    }
}