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
    private boolean ifMessageUnread;

    public Mail(User sender, User receiver, String message, boolean ifMessageUnread) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.ifMessageUnread = ifMessageUnread;
    }

    public void setIfMessageUnread(boolean ifMessageUnread) {
        this.ifMessageUnread = ifMessageUnread;
    }
}
