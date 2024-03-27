package mail;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MailBox {
    private List<Mail> mailList;
    private final int BOXLIMIT = 5;

    public MailBox() {
        this.mailList = new ArrayList<>();
    }

    public void receive(Mail mail){
        if(ifBoxFull()){
            throw new RuntimeException("Mailbox is full");
        } else {
            mailList.add(mail);
            System.out.println("You have a new unread message");
        }
    }

    public void delete(Mail mail){
        mailList.remove(mail);
        System.out.println("Message deleted successfully");
    }

    public boolean ifBoxFull(){
        return mailList.size() >= BOXLIMIT;
    }
}
