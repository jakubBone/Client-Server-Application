package mail;

import user.User;

public class MailService extends MailBox {

    public void sendMessage(User receiver, Mail mail){
        if(receiver.getMailBox().ifBoxFull())
            throw new RuntimeException("Unable to deliver email. The recipient's mailbox is full");
        else {
            receiver.getMailBox().receive(mail);
        }
    }

    public void readMessages(User user){
        for (Mail mail: user.getMailBox().getMailList()){
            System.out.println(mail.getMessage());
            mail.setIfMessageUnread(true);
            System.out.println("Email opened");
        }
    }

    public void deleteMessage(User user, Mail mail){
        user.getMailBox().delete(mail);
    }

}
