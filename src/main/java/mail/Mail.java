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
    private boolean isRead;
    private int messageLength;

    public Mail(User sender, User recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.isRead = false;
        this.messageLength = message.length();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
