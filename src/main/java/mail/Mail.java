package mail;

import user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private User sender;
    private User recipient;
    private String message;
    private int messageLength;

    public Mail(User sender, User recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageLength = message.length();
    }

}
