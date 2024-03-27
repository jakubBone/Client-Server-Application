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

    public void receive(Mail mail){
        if(ifBoxFull()){
            throw new RuntimeException("Mailbox is full");
        } else {
            mailList.add(mail);
            System.out.println("You have a new unread message";);
        }
    }

    public void delete(Mail mail){
        mailList.remove(mail);
        System.out.println("Message deleted successfully");
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
