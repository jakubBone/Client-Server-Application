package mail;

import mail.Mail;
import mail.MailBox;
import utils.User;

public class MailService extends MailBox {

    private void sendMessage(User receiver, Mail mail){
        if(receiver.getMailBox().ifBoxFull())
            throw new RuntimeException("Can't send mail. Receiver mailbox is full");
        else {
            receiver.getMailBox().receive(mail);
        }
    }

    private void readMessages(User user){
        for (Mail mail: user.getMailBox().getMailList()){
            System.out.println(mail.getMessage());
            mail.setIfMessageUnread(true);
        }
    }

    private void deleteMessage(User user, Mail mail){
        user.getMailBox().delete(mail);
    }

}
