package exceptions;

public class MailboxOverflowException extends Exception{
    public MailboxOverflowException(String message) {
        super(message);
    }

    public void handleException() {
        if (this.getMessage().equals("Unable to send message")) {
            System.err.println("Mailbox full: Unable to receive message");
        } else if (this.getMessage().equals("Unable to receive message")) {
            System.err.println("Mailbox full: Unable to receive message");
        } else {
            System.err.println("Unidentified error: " + this.getMessage());
        }
    }
}
