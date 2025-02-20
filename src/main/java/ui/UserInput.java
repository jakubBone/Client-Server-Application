package ui;

import java.io.BufferedReader;
import java.io.IOException;

public class UserInput {
    private BufferedReader reader;
    public UserInput(BufferedReader reader) {
        this.reader = reader;
    }

    public String promptUsername() throws IOException {
        System.out.print("Username: ");
        return reader.readLine();
    }

    public String promptPassword() throws IOException {
        System.out.print("Password: ");
        return reader.readLine();
    }

    public String promptNewPassword() throws IOException{
        System.out.print("New password: ");
        return reader.readLine();
    }

    public String promptRecipient() throws IOException {
        System.out.print("Send to: ");
        return reader.readLine();
    }

    public String promptMessage() throws IOException {
        System.out.println("Message (max 255 characters): ");
        return reader.readLine();
    }

    public String promptNewRole() throws IOException {
        System.out.print("New role (ADMIN / USER): ");
        return reader.readLine();
    }

    /*if(newRole.equals("ADMIN")){
        return Role.ADMIN;
    } else {
        return Role.USER;
    }*/

    // System.out.println("Invalid mailbox type. Please try again!");
    // System.out.println("Invalid operation. Please try again!");;
}