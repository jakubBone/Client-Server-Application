package mail;

import exceptions.MailboxOverflowException;
import user.User;

public class MailService {

    public void sendMessage(User receiver, Mail mail) throws MailboxOverflowException {
        if(receiver.getMailBox().ifBoxFull())
            throw new MailboxOverflowException("Unable to send message");
        else {
            receiver.getMailBox().receive(mail);
        }
    }

    public void readMessages(User user){
        for (Mail mail: user.getMailBox().getMailList()){
            System.out.println(mail.getMessage());
            mail.markAsRead();
            System.out.println("Message opened");
        }
    }

    public void deleteMessage(User user, Mail mail){
        user.getMailBox().delete(mail);
    }
}
