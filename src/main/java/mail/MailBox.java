package mail;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public class MailBox {

    private static final Logger logger = LogManager.getLogger(MailBox.class);
    private List<Mail> readMails;
    private List<Mail> unreadMails;
    private final int BOXLIMIT = 5;

    public MailBox() {
        this.readMails = new ArrayList<>();
        this.unreadMails = new ArrayList<>();
    }

    public boolean ifBoxFull(){
        return unreadMails.size() == BOXLIMIT;
    }
}
