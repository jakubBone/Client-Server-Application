package com.jakub.bone.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsolerReader {
    private final BufferedReader reader;

    public ConsolerReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getRequest() throws IOException {
        return reader.readLine();
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

    public String promptMailbox() throws IOException {
        System.out.print("Read: (INBOX / SENT): ");
        return reader.readLine();
    }

    public String promptMessageContent() throws IOException {
        System.out.print("Message content: ");
        return reader.readLine();
    }
}