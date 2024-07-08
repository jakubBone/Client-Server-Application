package response.mail;

import response.Response;
import mail.Mail;
import mail.MailService;
import request.Request;
import shared.ResponseStatus;

import java.util.List;
public class MailsReadResponse implements Response {
    private final MailService mailService;

    public MailsReadResponse(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String execute(Request request) {
        String boxType = request.getBoxType();
        List<Mail> mailsToRead = mailService.getMails(boxType);

        if (mailsToRead.isEmpty()) {
            return ResponseStatus.MAILBOX_EMPTY.getResponse();
        }

        StringBuilder response = new StringBuilder();
        for (Mail mail : mailsToRead) {
            response.append("From: ")
                    .append(mail.getSender().getUsername())
                    .append("\n Message: ")
                    .append(mail.getMessage())
                    .append("\n");
        }

        if (boxType.equals(Mail.Status.UNREAD.toString())) {
            mailService.markAsRead();
        }
        String mailBoxContent = boxType + " MAILS: \n" + response.toString();
        return  mailBoxContent;
    }
}