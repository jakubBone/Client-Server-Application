package mail;

import mail.Mail;

import java.util.ArrayList;
import java.util.List;

public class MailBox {
    private List<Mail> mailList;
    private final int BOXLIMIT = 5;

    public MailBox() {
        mailList = new ArrayList<>();
    }

    public String receive(Mail mail){
        if(ifBoxFull()){
            throw new RuntimeException("Mailbox is full");
        } else {
            mailList.add(mail);
        }
        return "You have a new unread message";
    }

    public String delete(Mail mail){
        mailList.remove(mail);
        return "Message deleted successfully";
    }

    public boolean ifBoxFull(){
        return mailList.size() >= BOXLIMIT;
    }

    public List<Mail> getMailList() {
        return mailList;
    }

    public void setMailList(List<Mail> mailList) {
        this.mailList = mailList;
    }

    public int getBOXLIMIT() {
        return BOXLIMIT;
    }
}
