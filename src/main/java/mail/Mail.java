package mail;

import user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private User sender;
    private User receiver;
    private String message;
    private boolean isRead;
    private int maxMessageLength;

    public Mail(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isRead = false;
        this.maxMessageLength = message.length();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
