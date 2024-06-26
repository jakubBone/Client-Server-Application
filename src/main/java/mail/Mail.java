package mail;

import lombok.Getter;
import lombok.Setter;
import user.User;
@Getter
@Setter
public class Mail {
    private User sender;
    private User recipient;
    private String message;

    public Mail(User sender, User recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }
}
