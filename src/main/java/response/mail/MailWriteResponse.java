package response.mail;

import response.Response;
import mail.MailService;
import request.Request;
import shared.ResponseStatus;
import user.credential.User;
import user.manager.UserManager;

public class MailWriteResponse implements Response {
    private final MailService mailService;
    private final UserManager userManager;

    public MailWriteResponse(MailService mailService, UserManager userManager) {
        this.mailService = mailService;
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        String username = request.getRecipient();
        String message = request.getMessage();
        User recipient = userManager.getUserByUsername(username);

        if (recipient == null) {
            return ResponseStatus.SENDING_FAILED_RECIPIENT_NOT_FOUND.getResponse();
        }

        if (mailService.isMailboxFull(recipient)) {
            return ResponseStatus.SENDING_FAILED_BOX_FULL.getResponse();
        }

        if (message.length() >= 255) {
            return ResponseStatus.SENDING_FAILED_TO_LONG_MESSAGE.getResponse();
        }

        mailService.sendMail(recipient, message);
        return ResponseStatus.SENDING_SUCCEEDED.getResponse();
    }
}