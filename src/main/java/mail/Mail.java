package mail;

import utils.User;

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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean IfMessageUnread() {
        return ifMessageUnread;
    }

    public void setIfMessageUnread(boolean ifMessageUnread) {
        this.ifMessageUnread = ifMessageUnread;
    }
}
