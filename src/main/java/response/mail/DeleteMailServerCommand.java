package response.mail;

import response.Response;
import mail.MailService;
import request.Request;
import servercommand.ServerCommand;
import utils.ResponseStatus;

public class DeleteMailServerCommand implements ServerCommand {
    private final MailService mailService;

    public DeleteMailServerCommand(MailService mailService) {
        this.mailService = mailService;
    }
    @Override
    public String execute(Request request) {
        String boxType = request.getBoxType();
        mailService.deleteMails(boxType);
        return ResponseStatus.MAIL_DELETION_SUCCEEDED.getResponse();
    }
}