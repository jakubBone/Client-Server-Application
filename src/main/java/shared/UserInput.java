package shared;

import java.io.BufferedReader;
import java.io.IOException;

import user.credential.User.Role;

public class UserInput {
    private BufferedReader reader;
    public UserInput(BufferedReader reader) {
        this.reader = reader;
    }

    public String getUsername() throws IOException {
        System.out.print("Please type your username: ");
        return reader.readLine();
    }

    public String getPassword() throws IOException {
        System.out.print("Please type your password: ");
        return reader.readLine();
    }

    public String getNewPassword() throws IOException{
        System.out.print("Please type a new password: ");
        return reader.readLine();
    }

    public String getRecipient() throws IOException {
        System.out.print("Please type a recipient's username: ");
        return reader.readLine();
    }

    public String getMessage() throws IOException {
        System.out.println("Please type your message (max 255 characters): ");
        return reader.readLine();
    }

     public String getUserToSwitch() throws IOException {
         System.out.print("Please type a username to switch: ");
         return reader.readLine();
     }

    public String chooseBoxOperation() throws IOException {
        while (true) {
            System.out.println("Mailbox operation: READ / DELETE: ");
            System.out.print("Select: ");
            String operation = reader.readLine().toUpperCase();
            switch (operation) {
                case "READ":
                case "DELETE":
                    return operation;
                default:
                    System.out.println("Invalid operation. Please try again!");;
            }
        }
    }
    public String chooseBoxType() throws IOException {
        while (true) {
                System.out.println("Mailbox type: OPENED / UNREAD / SENT");
                System.out.print("Select: ");
                String mailbox = reader.readLine().toUpperCase();
                switch (mailbox) {
                    case "OPENED":
                    case "UNREAD":
                    case "SENT":
                        return mailbox;
                    default:
                        System.out.println("Invalid mailbox type. Please try again!");
                }
        }
    }

    public String chooseUpdateOperation() throws IOException {
        while (true) {
            System.out.println("Update operations: PASSWORD / REMOVE / ROLE");;
            System.out.print("Select: ");;
            String input = reader.readLine().toUpperCase();
            switch (input) {
                case "PASSWORD":
                    return "PASSWORD";
                case "REMOVE":
                    return "REMOVE";
                case "ROLE":
                    return "ROLE";
                default:
                    System.out.println("Invalid operation type. Please try again!");
            }
        }
    }

    public String chooseUserToUpdate() throws IOException {
        System.out.print("Please type an username to update: ");
        return reader.readLine();
    }

    public Role chooseRole() throws IOException {
        System.out.print("Please type the new role (ADMIN / USER): ");
        String newRole = reader.readLine().toUpperCase();
        if(newRole.equals("ADMIN")){
            return Role.ADMIN;
        } else {
            return Role.USER;
        }
    }
}