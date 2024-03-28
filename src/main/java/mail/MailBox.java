package mail;

import java.util.ArrayList;
import java.util.List;

import exceptions.MailboxOverflowException;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public class MailBox {

    private static final Logger logger = LogManager.getLogger(MailBox.class);
    private List<Mail> mailList;
    private final int BOXLIMIT = 5;

    public MailBox() {
        this.mailList = new ArrayList<>();
    }

    public void receive(Mail mail) throws MailboxOverflowException{
        if(ifBoxFull()){
           throw new MailboxOverflowException("Unable to receive mail");
        } else {
            mailList.add(mail);
            logger.info("New unread mail");
        }
    }

    public void delete(Mail mail){
        mailList.remove(mail);
        logger.info("Mail deleted successfully");
    }

    public boolean ifBoxFull(){
        return mailList.size() >= BOXLIMIT;
    }
}
