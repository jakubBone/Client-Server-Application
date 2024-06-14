package mail;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailBox {
    private List<Mail> openedBox;
    private List<Mail> unreadBox;
    private List<Mail> sentBox;
    private final int BOXLIMIT = 5;

    public MailBox() {
        openedBox = new ArrayList<>();
        unreadBox = new ArrayList<>();
        sentBox = new ArrayList<>();
    }

    public boolean ifUnreadBoxFull(){
        return unreadBox.size() >= BOXLIMIT;
    }
}