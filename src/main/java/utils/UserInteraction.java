package utils;

import java.io.BufferedReader;
import java.io.IOException;

public class UserInteraction {
    private BufferedReader reader;
    public UserInteraction(BufferedReader reader) {
        this.reader = reader;
    }

    public String getUsername() throws IOException {
        System.out.println("Please enter your username:");
        return reader.readLine();
    }

    public String getPassword() throws IOException {
        System.out.println("Please enter your password:");
        return reader.readLine();
    }

    public String getNewPassword() throws IOException{
        System.out.println("Please enter a new password:");
        return reader.readLine();
    }

    public String getRecipient() throws IOException {
        System.out.println("Please enter the recipient's username:");
        return reader.readLine();
    }

    public String getMessage() throws IOException {
        System.out.println("Please type your message (max 255 characters):");
        return reader.readLine();
    }

    public String chooseMailBox() throws IOException {
        while (true) {
            System.out.println("Select an operation for the mailbox: READ / EMPTY");
            String operation = reader.readLine().toUpperCase();
            switch (operation) {
                case "READ":
                case "EMPTY":
                    System.out.println("Select the mailbox type: OPENED / UNREAD / SENT");
                    String mailbox = reader.readLine().toUpperCase();
                    switch (mailbox) {
                        case "OPENED":
                        case "UNREAD":
                        case "SENT":
                            return operation + " " + mailbox;
                        default:
                            System.out.println("Invalid mailbox type entered. Please try again.");
                            break;
                    }
                    break;
                default:
                    System.out.println("Invalid operation entered. Please try again.");;
                    break;
            }
        }
    }
    public String chooseAccountUpdate() throws IOException {
        while (true) {
            System.out.println("Choose account setting to update: PASSWORD / DELETE");;
            String input = reader.readLine();
            switch (input.toUpperCase()) {
                case "PASSWORD":
                    return "PASSWORD";
                case "DELETE":
                    return "DELETE";
                default:
                    System.out.println("Invalid input. Please enter either 'PASSWORD' or 'DELETE'.");
                    break;
            }
        }
    }

    public String chooseUserToUpdate() throws IOException {
        System.out.println("Please enter the username of the account to update:");
        return reader.readLine();
    }
}
