package mail;

import lombok.Getter;
import lombok.Setter;
import user.User;
@Getter
@Setter
public class Mail {

    public enum Status{
        UNREAD,
        OPENED,
        SENT;
    }

    private User sender;
    private User recipient;
    private String message;
    private Status status;

    public Mail(User sender, User recipient, String message, Status status) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.status = status;
    }
}
