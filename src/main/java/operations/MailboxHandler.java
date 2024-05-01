package operations;

import lombok.extern.log4j.Log4j2;
import mail.Mail;
import mail.MailService;

import java.io.IOException;
import java.util.List;
@Log4j2
public class MailboxHandler {
    private MailService mailService = new MailService();

    public String getMailboxResponse(String mailOperation, String boxType) throws IOException {
        String response = null;
        if(mailOperation.equals("READ")){
            response = getReadResposne(boxType);
        } else if(mailOperation.equals("EMPTY")){
            response = getEmptyMailboxResponse(boxType);
        }
        return response;
    }

    private String getReadResposne(String boxType){
        String response = null;
        List<Mail> mailsToRead = mailService.getMailsToRead(boxType);
        if(mailsToRead.isEmpty()){
            response = "Mailbox is empty";
        } else{
            for (Mail mail : mailsToRead) {
                response = "From: " + mail.getSender().getUsername() + "\n Message: " + mail.getMessage();
            }
            mailService.markMailsAsRead(boxType);
        }
        return response;
    }

    private String getEmptyMailboxResponse(String boxType){
        mailService.emptyMailbox(boxType);
        return "Mails deleted successfully";
    }
}
