package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UserInput {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String getRequest() throws IOException {
        return reader.readLine();
    }

    public String promptUsername() throws IOException {
        System.out.print("Username: ");
        return reader.readLine().toUpperCase();
    }

    public String promptPassword() throws IOException {
        System.out.print("Password: ");
        return reader.readLine().toUpperCase();
    }

    public String promptNewPassword() throws IOException{
        System.out.print("New password: ");
        return reader.readLine().toUpperCase();
    }

    public String promptRecipient() throws IOException {
        System.out.print("Send to: ");
        return reader.readLine().toUpperCase();
    }

    public String promptMessage() throws IOException {
        System.out.println("Message (max 255 characters): ");
        return reader.readLine();
    }

    public String promptNewRole() throws IOException {
        System.out.print("New role (ADMIN / USER): ");
        return reader.readLine().toUpperCase();
    }

    /*if(newRole.equals("ADMIN")){
        return Role.ADMIN;
    } else {
        return Role.USER;
    }*/

    // System.out.println("Invalid mailbox type. Please try again!");
    // System.out.println("Invalid operation. Please try again!");;
}