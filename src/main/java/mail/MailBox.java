package mail;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailBox {
    private List<Mail> openedMails;
    private List<Mail> unreadMails;
    private List<Mail> sentMails;
    private final int BOXLIMIT = 5;

    public MailBox() {
        openedMails = new ArrayList<>();
        unreadMails = new ArrayList<>();
        sentMails = new ArrayList<>();
    }

    public boolean ifBoxFull(){
        return unreadMails.size() == BOXLIMIT;
    }
}
